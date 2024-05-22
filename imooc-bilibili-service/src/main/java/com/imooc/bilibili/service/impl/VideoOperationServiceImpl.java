package com.imooc.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.domain.video.VideoOperation;
import com.imooc.bilibili.mapper.VideoOperationMapper;
import com.imooc.bilibili.service.VideoOperationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class VideoOperationServiceImpl extends ServiceImpl<VideoOperationMapper, VideoOperation> implements VideoOperationService {
    @Resource
    private VideoOperationMapper videoOperationMapper;
}
