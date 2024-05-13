package com.imooc.bilibili.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.domain.FileMetadata;
import com.imooc.bilibili.exception.ConditionException;
import com.imooc.bilibili.mapper.FileMetadataMapper;
import com.imooc.bilibili.service.FileMetadataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FileMetadataServiceImpl extends ServiceImpl<FileMetadataMapper, FileMetadata> implements FileMetadataService {
    @Resource
    private FileMetadataMapper fileMetadataMapper;

    @Override
    public Integer addFileMetadata(FileMetadata fileMetadata) {
        return fileMetadataMapper.insert(fileMetadata);
    }

    @Override
    public FileMetadata getFileMetadataByMd5(String fileMd5) {
        if (StrUtil.isBlankOrUndefined(fileMd5)) {
            throw new ConditionException("md5值为空,无效参数");
        }
        LambdaQueryWrapper<FileMetadata> queryWrapper = new LambdaQueryWrapper<FileMetadata>()
                .eq(FileMetadata::getFileMd5, fileMd5);
        return fileMetadataMapper.selectOne(queryWrapper);
    }
}
