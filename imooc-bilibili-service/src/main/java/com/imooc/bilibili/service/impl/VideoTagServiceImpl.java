package com.imooc.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.domain.video.VideoTag;
import com.imooc.bilibili.mapper.VideoTagMapper;
import com.imooc.bilibili.service.VideoTagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class VideoTagServiceImpl extends ServiceImpl<VideoTagMapper, VideoTag> implements VideoTagService {
    @Resource
    private VideoTagMapper videoTagMapper;
}
