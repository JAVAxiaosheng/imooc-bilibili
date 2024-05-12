package com.imooc.bilibili.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import com.imooc.bilibili.exception.ConditionException;
import com.imooc.bilibili.service.FastDfsService;
import com.imooc.bilibili.util.FastDFSUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Map;

@Service
public class FastDfsServiceImpl implements FastDfsService {

    @Resource
    private FastDFSUtil fastDFSUtil;

    // 分片上传文件
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadAppenderFile(MultipartFile multipartFile) throws Exception {
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
        File tempFile = fastDFSUtil.multipartFileToFile(multipartFile);
        String md5 = SecureUtil.md5(tempFile);
        FileUtil.del(tempFile);
        int count = 1;
        while (count <= fileSliceTotal) {
            File sliceFile = sliceFileMap.get(count);
            MultipartFile sliceMultipartFile = fastDFSUtil.fileToMultipartFile(sliceFile);
            path = fastDFSUtil.uploadFileBySlices(sliceMultipartFile, md5,
                    count, fileSliceTotal);
            count++;
        }
        FileUtil.clean(FastDFSUtil.FILE_SLICES_TEMP_PATH);
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
