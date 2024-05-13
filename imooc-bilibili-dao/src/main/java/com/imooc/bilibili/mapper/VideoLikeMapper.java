package com.imooc.bilibili.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.bilibili.domain.video.VideoLike;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VideoLikeMapper extends BaseMapper<VideoLike> {
}
