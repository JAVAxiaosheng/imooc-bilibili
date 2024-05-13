package com.imooc.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.domain.video.VideoLike;
import com.imooc.bilibili.mapper.VideoLikeMapper;
import com.imooc.bilibili.service.VideoLikeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class VideoLikeServiceImpl extends ServiceImpl<VideoLikeMapper, VideoLike> implements VideoLikeService {
    @Resource
    private VideoLikeMapper videoLikeMapper;


    @Override
    public VideoLike getVideoLikeByVideoIdAndUserId(Long videoId, Long userId) {
        LambdaQueryWrapper<VideoLike> queryWrapper = new LambdaQueryWrapper<VideoLike>()
                .eq(VideoLike::getVideoId, videoId)
                .eq(VideoLike::getUserId, userId);
        return videoLikeMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer deleteVideoLikeByVideoIdAndUserId(Long videoId, Long userId) {
        LambdaQueryWrapper<VideoLike> queryWrapper = new LambdaQueryWrapper<VideoLike>()
                .eq(VideoLike::getVideoId, videoId)
                .eq(VideoLike::getUserId, userId);
        return videoLikeMapper.delete(queryWrapper);
    }

    @Override
    public Long getVideoLikeCountByVideoId(Long videoId) {
        LambdaQueryWrapper<VideoLike> queryWrapper = new LambdaQueryWrapper<VideoLike>()
                .eq(VideoLike::getVideoId, videoId);
        return videoLikeMapper.selectCount(queryWrapper);
    }
}
