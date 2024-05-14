package com.imooc.bilibili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.bilibili.domain.video.VideoCollection;

public interface VideoCollectionService extends IService<VideoCollection> {
    void addVideoCollection(VideoCollection videoCollection);

    void deleteVideoCollection(Long videoId, Long userId);

    Long getVideoCollectionCountByVideoId(Long videoId);

    VideoCollection getVideoCollectionByVideoIdAndUserId(Long videoId, Long userId);
}
