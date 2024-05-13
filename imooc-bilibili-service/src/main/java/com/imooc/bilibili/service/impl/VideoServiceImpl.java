package com.imooc.bilibili.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.domain.PageResult;
import com.imooc.bilibili.domain.video.Video;
import com.imooc.bilibili.domain.video.VideoLike;
import com.imooc.bilibili.domain.video.VideoTag;
import com.imooc.bilibili.exception.ConditionException;
import com.imooc.bilibili.mapper.VideoMapper;
import com.imooc.bilibili.service.VideoLikeService;
import com.imooc.bilibili.service.VideoService;
import com.imooc.bilibili.service.VideoTagService;
import com.imooc.bilibili.util.FastDFSUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
@Slf4j
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {
    @Resource
    private VideoMapper videoMapper;

    @Resource
    private VideoTagService videoTagService;

    @Resource
    private VideoLikeService videoLikeService;

    @Resource
    private FastDFSUtil fastDFSUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addVideo(Video video) {
        video.setCreateTime(new Date());
        videoMapper.insert(video);
        Long videoId = video.getId();
        List<VideoTag> videoTagList = video.getVideoTagList();
        videoTagList.forEach(item -> {
            item.setCreateTime(new Date());
            item.setVideoId(videoId);
        });
        videoTagService.saveBatch(videoTagList);
    }

    @Override
    public PageResult<Video> pageListVideos(Integer pageNum, Integer pageSize, String area) {
        if (pageNum == null || pageSize == null) {
            throw new ConditionException("分页参数异常");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("start", (pageNum - 1) * pageSize);
        params.put("limit", pageSize);
        params.put("area", area);
        List<Video> list = new ArrayList<>();
        Integer total = videoMapper.pageCountVideos(params);
        if (total > 0) {
            list = videoMapper.pageListVideos(params);
        }
        return new PageResult<>(total, list);
    }

    @Override
    public void viewVideoOnlineBySlices(HttpServletRequest request, HttpServletResponse response, String url) {
        try {
            fastDFSUtil.viewVideoOnlineBySlices(request, response, url);
        } catch (Exception e) {
            log.error("请求处理异常", e);
        }
    }

    @Override
    public void addVideoLike(Long userId, Long videoId) {
        // 获取到当前视频信息
        Video video = videoMapper.selectById(videoId);
        if (ObjectUtil.isNull(video)) {
            throw new ConditionException("没有视频相关信息");
        }
        // 获取点赞表,查看是否已经点过赞
        VideoLike videoLike = videoLikeService.getVideoLikeByVideoIdAndUserId(videoId, userId);
        if (ObjectUtil.isNotNull(video)) {
            throw new ConditionException("该视频已经点赞过");
        }
        videoLike = new VideoLike();
        videoLike.setUserId(userId);
        videoLike.setVideoId(videoId);
        videoLike.setCreateTime(new Date());
        videoLikeService.save(videoLike);
    }

    @Override
    public void deleteVideoLike(Long userId, Long videoId) {
        videoLikeService.deleteVideoLikeByVideoIdAndUserId(videoId, userId);
    }

    @Override
    public Map<String, Object> getVideoLikeCount(Long videoId, Long userId) {
        Long videoLikeCount = videoLikeService.getVideoLikeCountByVideoId(videoId);
        VideoLike videoLike = videoLikeService.getVideoLikeByVideoIdAndUserId(videoId, userId);
        boolean like = ObjectUtil.isNotNull(videoLike);
        Map<String, Object> result = new HashMap<>();
        result.put("count", videoLikeCount);
        result.put("like", like);
        return result;
    }
}
