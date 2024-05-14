package com.imooc.bilibili.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.bilibili.domain.video.VideoComment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface VideoCommentMapper extends BaseMapper<VideoComment> {
    Integer pageCountVideoComment(Map<String, Object> params);

    List<VideoComment> pageListVideoComment(Map<String, Object> params);

    List<VideoComment> batchGetVideoCommentByRootId(List<Long> parentIdList);
}
