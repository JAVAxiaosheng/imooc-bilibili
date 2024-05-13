package com.imooc.bilibili.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import com.imooc.bilibili.domain.FileMetadata;
import com.imooc.bilibili.exception.ConditionException;
import com.imooc.bilibili.service.FastDfsService;
import com.imooc.bilibili.service.FileMetadataService;
import com.imooc.bilibili.util.FastDFSUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.Map;

@Service
public class FastDfsServiceImpl implements FastDfsService {

    @Resource
    private FastDFSUtil fastDFSUtil;

    @Resource
    private FileMetadataService fileMetadataService;

    // 分片上传文件
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public String uploadAppenderFile(MultipartFile multipartFile) throws Exception {
        // 获取文件的md5
        String fileMd5 = SecureUtil.md5(multipartFile.getInputStream());
        // 根据md5查询数据库,存在就不需要再传
        FileMetadata fileMetadata = fileMetadataService.getFileMetadataByMd5(fileMd5);
        if (ObjectUtil.isNotNull(fileMetadata)) {
            // 秒传
            return fileMetadata.getDfsUrl();
        }

        // 查看分片目录下是否存在文件
        File[] fileSlices = FileUtil.ls(FastDFSUtil.FILE_SLICES_TEMP_PATH);
        if (fileSlices.length > 0) {
            FileUtil.clean(FastDFSUtil.FILE_SLICES_TEMP_PATH);
        }
        // 对文件进行分片处理
        Map<Integer, File> sliceFileMap = fastDFSUtil.convertFileToSlices(multipartFile);
        int fileSliceTotal = sliceFileMap.size();
        fileSlices = FileUtil.ls(FastDFSUtil.FILE_SLICES_TEMP_PATH);
        // 校验分片是否成功
        if (fileSlices.length != fileSliceTotal) {
            throw new ConditionException("文件分片失败,请重新上传");
        }
        //遍历分片文件列表,分片上传
        String path = "";
        int count = 1;
        while (count <= fileSliceTotal) {
            File sliceFile = sliceFileMap.get(count);
            MultipartFile sliceMultipartFile = fastDFSUtil.fileToMultipartFile(sliceFile);
            path = fastDFSUtil.uploadFileBySlices(sliceMultipartFile, fileMd5,
                    count, fileSliceTotal);
            count++;
        }
        FileUtil.clean(FastDFSUtil.FILE_SLICES_TEMP_PATH);

        // 文件上传完毕,存储文件的元数据
        FileMetadata metadata = new FileMetadata();
        metadata.setFileMd5(fileMd5);
        metadata.setDfsUrl(path);
        metadata.setFileType(fastDFSUtil.getFileType(multipartFile));
        metadata.setFileOriginName(multipartFile.getOriginalFilename());
        metadata.setCreateTime(new Date());
        fileMetadataService.addFileMetadata(metadata);
        return path;
    }

    @Override
    public String uploadFileSlices(MultipartFile sliceFile, String md5,
                                   Integer sliceNum, Integer totalSliceNum) throws Exception {
        return fastDFSUtil.uploadFileBySlices(sliceFile, md5, sliceNum, totalSliceNum);
    }

    @Override
    public String convertFileToSlices(MultipartFile multipartFile) throws Exception {
        String md5 = SecureUtil.md5(fastDFSUtil.multipartFileToFile(multipartFile));
        fastDFSUtil.convertFileToSlices(multipartFile);
        return md5;
    }
}
