package com.imooc.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.domain.video.VideoView;
import com.imooc.bilibili.mapper.VideoViewMapper;
import com.imooc.bilibili.service.VideoViewService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class VideoViewServiceImpl extends ServiceImpl<VideoViewMapper, VideoView> implements VideoViewService {
    @Resource
    private VideoViewMapper videoViewMapper;

    @Override
    public void addVideoView(VideoView videoView) {
        videoViewMapper.insert(videoView);
    }

    @Override
    public VideoView getVideoView(Map<String, Object> params) {
        return videoViewMapper.getVideoView(params);
    }

    @Override
    public Long getVideoViewCount(Long videoId) {
        LambdaQueryWrapper<VideoView> queryWrapper = new LambdaQueryWrapper<VideoView>()
                .eq(VideoView::getVideoId, videoId);
        return videoViewMapper.selectCount(queryWrapper);
    }
}
