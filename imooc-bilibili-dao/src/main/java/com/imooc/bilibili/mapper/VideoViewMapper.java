package com.imooc.bilibili.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.bilibili.domain.video.VideoView;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface VideoViewMapper extends BaseMapper<VideoView> {
    VideoView getVideoView(Map<String, Object> params);
}
