package com.imooc.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.domain.video.VideoComment;
import com.imooc.bilibili.mapper.VideoCommentMapper;
import com.imooc.bilibili.service.VideoCommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class VideoCommentServiceImpl extends ServiceImpl<VideoCommentMapper, VideoComment> implements VideoCommentService {
    @Resource
    private VideoCommentMapper videoCommentMapper;

    @Override
    public void addVideoComment(VideoComment videoComment) {
        videoCommentMapper.insert(videoComment);
    }

    @Override
    public Integer pageCountVideoComment(Map<String, Object> params) {
        return videoCommentMapper.pageCountVideoComment(params);
    }

    @Override
    public List<VideoComment> pageListVideoComment(Map<String, Object> params) {
        return videoCommentMapper.pageListVideoComment(params);
    }

    @Override
    public List<VideoComment> batchGetVideoCommentByRootId(List<Long> parentIdList) {
        return videoCommentMapper.batchGetVideoCommentByRootId(parentIdList);
    }
}
