package com.imooc.bilibili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.bilibili.domain.video.VideoLike;

public interface VideoLikeService extends IService<VideoLike> {
    VideoLike getVideoLikeByVideoIdAndUserId(Long videoId, Long userId);

    Integer deleteVideoLikeByVideoIdAndUserId(Long videoId, Long userId);

    Long getVideoLikeCountByVideoId(Long videoId);
}
