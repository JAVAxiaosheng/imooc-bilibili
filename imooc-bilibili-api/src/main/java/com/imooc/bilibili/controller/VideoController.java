package com.imooc.bilibili.controller;

import com.imooc.bilibili.domain.JsonResponse;
import com.imooc.bilibili.domain.PageResult;
import com.imooc.bilibili.domain.video.Video;
import com.imooc.bilibili.service.VideoService;
import com.imooc.bilibili.support.UserSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/video")
@Api(tags = "视频操作", produces = MediaType.APPLICATION_JSON_VALUE)
public class VideoController {
    @Resource
    private VideoService videoService;

    @Resource
    private UserSupport userSupport;


    @PostMapping("/add/video")
    @ApiOperation(value = "添加视频", httpMethod = "POST")
    public JsonResponse<String> addVideo(@RequestBody Video video) {
        Long userId = userSupport.getCurrentUserId();
        video.setUserId(userId);
        videoService.addVideo(video);
        return JsonResponse.success();
    }

    @GetMapping("/get/page/videos")
    @ApiOperation(value = "添加视频", httpMethod = "GET")
    public JsonResponse<PageResult<Video>> pageListVideos(@RequestParam("pageNum") Integer pageNum,
                                                          @RequestParam("pageSize") Integer pageSize,
                                                          @RequestParam("area") String area) {
        PageResult<Video> result = videoService.pageListVideos(pageNum, pageSize, area);
        return new JsonResponse<>(result);
    }

    @GetMapping("/video/slices")
    @ApiOperation(value = "视频分片", httpMethod = "GET")
    public void viewVideoOnlineBySlices(HttpServletRequest request,
                                        HttpServletResponse response,
                                        String url) {
        videoService.viewVideoOnlineBySlices(request, response, url);
    }

    @PostMapping("/video/like")
    @ApiOperation(value = "点赞视频", httpMethod = "POST")
    public JsonResponse<String> addVideoLike(@RequestParam Long videoId) {
        Long userId = userSupport.getCurrentUserId();
        videoService.addVideoLike(userId, videoId);
        return JsonResponse.success();
    }

    @DeleteMapping("/cancel/video/like")
    @ApiOperation(value = "取消点赞视频", httpMethod = "DELETE")
    public JsonResponse<String> deleteVideoLike(@RequestParam Long videoId) {
        Long userId = userSupport.getCurrentUserId();
        videoService.deleteVideoLike(userId, videoId);
        return JsonResponse.success();
    }

    @GetMapping("/video/like/count")
    @ApiOperation(value = "查看视频点赞数量", httpMethod = "GET")
    public JsonResponse<Map<String, Object>> getVideoLikeCount(@RequestParam Long videoId) {
        // 用户不登录也可以查看视频的点赞数量
        Long userId = null;
        try {
            userId = userSupport.getCurrentUserId();
        } catch (Exception ignored) {
        }
        Map<String, Object> result = videoService.getVideoLikeCount(videoId, userId);
        return new JsonResponse<>(result);
    }
}
