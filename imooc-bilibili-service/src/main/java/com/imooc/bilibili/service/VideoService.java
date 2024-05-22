package com.imooc.bilibili.service;

import cn.hutool.http.server.HttpServerRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.bilibili.domain.PageResult;
import com.imooc.bilibili.domain.video.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface VideoService extends IService<Video> {
    void addVideo(Video video);

    PageResult<Video> pageListVideos(Integer pageNum, Integer pageSize, String area);

    void viewVideoOnlineBySlices(HttpServletRequest request, HttpServletResponse response, String url);

    void addVideoLike(Long userId, Long videoId);

    void deleteVideoLike(Long userId, Long videoId);

    Map<String, Object> getVideoLikeCount(Long videoId, Long userId);

    void addVideoCollection(VideoCollection videoCollection);

    void deleteVideoCollection(Long videoId, Long userId);

    Map<String, Object> getVideoCollectionCount(Long videoId, Long userId);

    void addVideoCoin(VideoCoin videoCoin);

    Map<String, Object> getVideoCoinCount(Long videoId, Long userId);

    void addVideoComment(VideoComment videoComment);

    PageResult<VideoComment> pageListVideoComment(Integer pageSize, Integer pageNum, Long videoId);

    Map<String, Object> getVideoDetail(Long videoId);

    void addVideoView(VideoView videoView, HttpServletRequest request);

    Long getVideoViewCount(Long videoId);

    List<Video> recommend(Long userId);
}
