package com.imooc.bilibili.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.bilibili.domain.video.VideoCoin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface VideoCoinMapper extends BaseMapper<VideoCoin> {
    Long getVideoCoinAmountByVideoId(@Param("videoId") Long videoId);
}
