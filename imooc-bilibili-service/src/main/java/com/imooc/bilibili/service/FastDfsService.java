package com.imooc.bilibili.service;

import org.springframework.web.multipart.MultipartFile;

public interface FastDfsService {
    String uploadAppenderFile(MultipartFile file) throws Exception;

    String uploadFileSlices(MultipartFile sliceFile, String md5,
                            Integer sliceNum, Integer totalSliceNum) throws Exception;

    String convertFileToSlices(MultipartFile multipartFile) throws Exception;
}
