package com.imooc.bilibili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.bilibili.domain.video.VideoComment;

import java.util.List;
import java.util.Map;

public interface VideoCommentService extends IService<VideoComment> {
    void addVideoComment(VideoComment videoComment);

    Integer pageCountVideoComment(Map<String, Object> params);

    List<VideoComment> pageListVideoComment(Map<String, Object> params);

    List<VideoComment> batchGetVideoCommentByRootId(List<Long> parentIdList);
}
