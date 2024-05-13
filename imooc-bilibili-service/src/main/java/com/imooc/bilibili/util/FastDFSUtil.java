package com.imooc.bilibili.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.github.tobato.fastdfs.domain.fdfs.FileInfo;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.google.common.base.Strings;
import com.imooc.bilibili.exception.ConditionException;
import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Component
@Slf4j
public class FastDFSUtil {
    private static final String FAST_DFS_DEFAULT_GROUP_NAME = "group1";
    private static final String PATH_KEY = "path-key:";
    private static final String UPLOADED_SIZE_KEY = "uploaded-size-key:";
    private static final String UPLOADED_NUM_KEY = "uploaded-num-key:";
    private static final Integer SLICE_SIZE = 1024 * 1024;
    public static final String FILE_SLICES_TEMP_PATH = "D:\\Linux\\temp\\";
    public static final String TEMP_FILE_PATH = "D:\\Linux\\";

    @Value("${fdfs.http.storage-addr}")
    private String httpStorageAddr;

    @Resource
    private FastFileStorageClient fastFileStorageClient;

    // 断点续传服务接口
    @Resource
    private AppendFileStorageClient appendFileStorageClient;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    public String getFileType(MultipartFile file) {
        if (file == null) {
            throw new ConditionException("非法文件!");
        }
        String filename = file.getOriginalFilename();
        if (Strings.isNullOrEmpty(filename)) {
            throw new ConditionException("文件名为空!");
        }
        String fileType = "";
        int index = filename.lastIndexOf(".");
        if (index == -1) {
            fileType = null;
        } else {
            fileType = filename.substring(index + 1);
        }
        return fileType;
    }

    // 上传文件
    public String uploadCommonFile(MultipartFile file) throws Exception {
        Set<MetaData> metaDataSet = new HashSet<>();
        MetaData metaData = new MetaData();
        metaData.setName(file.getOriginalFilename());
        metaDataSet.add(metaData);
        String fileType = getFileType(file);
        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(),
                file.getSize(), fileType, metaDataSet);
        return storePath.getPath();
    }

    // 删除远端文件
    public void deleteCommonFile(String path) {
        fastFileStorageClient.deleteFile(path);
    }

    /**
     * 断点续传
     * 解决网络影响等原因中断上传问题
     * 把大文件进行分片化处理,依次上传,可从断点处开始上传后续数据
     * 分片操作可交由前端处理
     */
    public String uploadAppendCommonFile(MultipartFile file) throws Exception {
        StorePath storePath = appendFileStorageClient.uploadAppenderFile(FAST_DFS_DEFAULT_GROUP_NAME,
                file.getInputStream(), file.getSize(), FileUtil.extName(file.getOriginalFilename()));
        return storePath.getPath();
    }

    // offset:偏移量
    public void modifyAppendCommonFile(MultipartFile file, String path, Long offset) throws Exception {
        appendFileStorageClient.modifyFile(FAST_DFS_DEFAULT_GROUP_NAME, path,
                file.getInputStream(), file.getSize(), offset);
    }

    // 分片上传
    public String uploadFileBySlices(MultipartFile file, String fileMd5, Integer sliceNum, Integer totalSliceNum) throws Exception {
        if (file == null || sliceNum == null || totalSliceNum == null) {
            throw new ConditionException("参数异常!");
        }
        String pathKey = PATH_KEY + fileMd5;
        String uploadedSizeKey = UPLOADED_SIZE_KEY + fileMd5;
        String uploadedNumKey = UPLOADED_NUM_KEY + fileMd5;

        String uploadedSizeStr = redisTemplate.opsForValue().get(uploadedSizeKey);
        Long uploadedSize = 0L;
        if (!StringUtils.isNullOrEmpty(uploadedSizeStr)) {
            uploadedSize = Long.valueOf(uploadedSizeStr);
        }
        String filePath = "";
        // 上传第一个分片
        if (sliceNum == 1) {
            filePath = uploadAppendCommonFile(file);
            if (StringUtils.isNullOrEmpty(filePath)) {
                throw new ConditionException("上传失败");
            }
            redisTemplate.opsForValue().set(pathKey, filePath);
            redisTemplate.opsForValue().set(uploadedNumKey, String.valueOf(sliceNum));
        } else {
            filePath = redisTemplate.opsForValue().get(pathKey);
            if (StringUtils.isNullOrEmpty(filePath)) {
                throw new ConditionException("上传失败");
            }
            modifyAppendCommonFile(file, filePath, uploadedSize);
            redisTemplate.opsForValue().set(uploadedNumKey, String.valueOf(sliceNum));
        }
        // 修改分片文件大小
        uploadedSize += file.getSize();
        redisTemplate.opsForValue().set(uploadedSizeKey, String.valueOf(uploadedSize));
        // 如果所有分片上传完成,清空redis
        String uploadedNumStr = redisTemplate.opsForValue().get(uploadedNumKey);
        if (StringUtils.isNullOrEmpty(uploadedNumStr)) {
            throw new ConditionException("上传失败");
        }
        Integer uploadedNum = Integer.valueOf(uploadedNumStr);
        if (uploadedNum.equals(totalSliceNum)) {
            List<String> keyList = Arrays.asList(uploadedSizeKey, uploadedNumKey, pathKey);
            redisTemplate.delete(keyList);
        }
        return filePath;
    }

    // 对文件进行分片
    public Map<Integer, File> convertFileToSlices(MultipartFile multipartFile) throws Exception {
        // 先清空目标目录下的文件
        File[] fileSlices = FileUtil.ls(FastDFSUtil.FILE_SLICES_TEMP_PATH);
        if (fileSlices.length > 0) {
            FileUtil.clean(FastDFSUtil.FILE_SLICES_TEMP_PATH);
        }

        String fileType = getFileType(multipartFile);
        File file = multipartFileToFile(multipartFile);
        long fileLength = file.length();
        int count = 1;
        Map<Integer, File> resultMap = new HashMap<>();
        for (int i = 0; i < fileLength; i += SLICE_SIZE) {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(i);
            byte[] bytes = new byte[SLICE_SIZE];
            int len = randomAccessFile.read(bytes);
            String path = FILE_SLICES_TEMP_PATH + count + "." + fileType;
            File sliceFile = new File(path);
            FileOutputStream fos = new FileOutputStream(sliceFile);
            fos.write(bytes, 0, len);
            fos.close();
            randomAccessFile.close();
            resultMap.put(count, sliceFile);
            count++;
        }
        FileUtil.del(file);
        return resultMap;
    }

    public File multipartFileToFile(MultipartFile multipartFile) throws Exception {
        String originalFilename = multipartFile.getOriginalFilename();
        if (StringUtils.isNullOrEmpty(originalFilename)) {
            throw new ConditionException("文件名为空,参数异常!");
        }
        int index = originalFilename.lastIndexOf(".");
        String fileName = "";
        if (index == -1) {
            fileName = originalFilename;
        } else {
            fileName = originalFilename.substring(0, index);
        }
        String fileType = getFileType(multipartFile);
        File file = new File(TEMP_FILE_PATH + fileName + "." + fileType);
        if (file.exists()) {
            FileUtil.del(file);
        }
        FileUtil.touch(file);
        FileCopyUtils.copy(multipartFile.getBytes(), file);
        return file;
    }

    public MultipartFile fileToMultipartFile(File file) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem(file.getName(), "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return new CommonsMultipartFile(item);
    }

    // 视频在线观看
    public void viewVideoOnlineBySlices(HttpServletRequest request, HttpServletResponse response,
                                        String path) throws Exception {
        FileInfo fileInfo = fastFileStorageClient.queryFileInfo(FAST_DFS_DEFAULT_GROUP_NAME, path);
        long fileTotalFileSize = fileInfo.getFileSize();
        String url = httpStorageAddr + path;
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, Object> headers = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            headers.put(header, request.getHeader(header));
        }
        String rangeStr = request.getHeader("Range");
        if (StrUtil.isEmptyOrUndefined(rangeStr)) {
            rangeStr = "bytes=0-" + (fileTotalFileSize - 1);
        }
        String[] ranges = rangeStr.split("bytes=|-");
        long begin = 0;
        long end = fileTotalFileSize - 1;
        if (ranges.length >= 2) {
            begin = Long.parseLong(ranges[1]);
        }
        if (ranges.length >= 3) {
            end = Long.parseLong(ranges[2]);
        }
        long slicesLen = (end - begin) + 1;
        String contentRange = "bytes " + begin + "-" + end + "/" + fileTotalFileSize;
        response.setHeader("Content-Range", contentRange);
        response.setHeader("Accept-Ranges", "bytes");
        response.setContentLength((int) slicesLen);
        response.setHeader("Content-Type", "video/mp4");
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        HttpUtil.get(url, headers, response);
    }
}
