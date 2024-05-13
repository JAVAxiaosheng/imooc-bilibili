package com.imooc.bilibili.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.imooc.bilibili.domain.video.VideoTag;

@Mapper
public interface VideoTagMapper extends BaseMapper<VideoTag> {
}
