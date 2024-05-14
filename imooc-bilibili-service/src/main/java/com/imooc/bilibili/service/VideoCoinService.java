package com.imooc.bilibili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.bilibili.domain.video.VideoCoin;

public interface VideoCoinService extends IService<VideoCoin> {
    void addVideoCoin(VideoCoin videoCoin);

    VideoCoin getVideoCoinByVideoIdAndUserId(Long videoId, Long userId);

    void updateVideoCoinById(VideoCoin videoCoin);

    Long getVideoCoinAmountByVideoId(Long videoId);
}
