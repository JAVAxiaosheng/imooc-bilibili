package com.imooc.bilibili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.bilibili.domain.video.VideoView;

import java.util.Map;

public interface VideoViewService extends IService<VideoView> {
    void addVideoView(VideoView videoView);

    VideoView getVideoView(Map<String, Object> params);

    Long getVideoViewCount(Long videoId);
}
