package com.imooc.bilibili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.bilibili.domain.FileMetadata;

public interface FileMetadataService extends IService<FileMetadata> {
    Integer addFileMetadata(FileMetadata fileMetadata);

    FileMetadata getFileMetadataByMd5(String fileMd5);
}
