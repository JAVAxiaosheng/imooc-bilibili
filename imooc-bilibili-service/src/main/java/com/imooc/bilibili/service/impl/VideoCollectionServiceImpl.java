package com.imooc.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.domain.video.VideoCollection;
import com.imooc.bilibili.mapper.VideoCollectionMapper;
import com.imooc.bilibili.service.VideoCollectionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class VideoCollectionServiceImpl extends ServiceImpl<VideoCollectionMapper, VideoCollection> implements VideoCollectionService {
    @Resource
    private VideoCollectionMapper videoCollectionMapper;

    @Override
    public void addVideoCollection(VideoCollection videoCollection) {
        videoCollectionMapper.insert(videoCollection);
    }

    @Override
    public void deleteVideoCollection(Long videoId, Long userId) {
        LambdaQueryWrapper<VideoCollection> queryWrapper = new LambdaQueryWrapper<VideoCollection>()
                .eq(VideoCollection::getVideoId, videoId)
                .eq(VideoCollection::getUserId, userId);
        videoCollectionMapper.delete(queryWrapper);
    }

    @Override
    public Long getVideoCollectionCountByVideoId(Long videoId) {
        LambdaQueryWrapper<VideoCollection> queryWrapper = new LambdaQueryWrapper<VideoCollection>()
                .eq(VideoCollection::getVideoId, videoId);
        return videoCollectionMapper.selectCount(queryWrapper);
    }

    @Override
    public VideoCollection getVideoCollectionByVideoIdAndUserId(Long videoId, Long userId) {
        LambdaQueryWrapper<VideoCollection> queryWrapper = new LambdaQueryWrapper<VideoCollection>()
                .eq(VideoCollection::getVideoId, videoId)
                .eq(VideoCollection::getUserId, userId);
        return videoCollectionMapper.selectOne(queryWrapper);
    }
}
