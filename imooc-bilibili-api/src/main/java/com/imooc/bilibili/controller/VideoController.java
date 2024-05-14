package com.imooc.bilibili.controller;

import com.imooc.bilibili.domain.JsonResponse;
import com.imooc.bilibili.domain.PageResult;
import com.imooc.bilibili.domain.video.Video;
import com.imooc.bilibili.domain.video.VideoCoin;
import com.imooc.bilibili.domain.video.VideoCollection;
import com.imooc.bilibili.domain.video.VideoComment;
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


    @PostMapping("/video/collection")
    @ApiOperation(value = "收藏视频", httpMethod = "POST")
    public JsonResponse<String> addVideoCollection(@RequestBody VideoCollection videoCollection) {
        Long userId = userSupport.getCurrentUserId();
        videoCollection.setUserId(userId);
        videoService.addVideoCollection(videoCollection);
        return JsonResponse.success();
    }

    @DeleteMapping("/cancel/video/collection")
    @ApiOperation(value = "取消视频收藏", httpMethod = "DELETE")
    public JsonResponse<String> deleteVideoCollection(@RequestParam Long videoId) {
        Long userId = userSupport.getCurrentUserId();
        videoService.deleteVideoCollection(videoId, userId);
        return JsonResponse.success();
    }

    @GetMapping("/video/collection/count")
    @ApiOperation(value = "查看视频收藏数量", httpMethod = "GET")
    public JsonResponse<Map<String, Object>> getVideoCollectionCount(@RequestParam Long videoId) {
        Long userId = null;
        try {
            userId = userSupport.getCurrentUserId();
        } catch (Exception ignored) {
        }
        Map<String, Object> result = videoService.getVideoCollectionCount(videoId, userId);
        return new JsonResponse<>(result);
    }

    @PostMapping("/video/coin")
    @ApiOperation(value = "视频投币", httpMethod = "POST")
    public JsonResponse<String> addVideoCoin(@RequestBody VideoCoin videoCoin) {
        Long userId = userSupport.getCurrentUserId();
        videoCoin.setUserId(userId);
        videoService.addVideoCoin(videoCoin);
        return JsonResponse.success();
    }

    @GetMapping("/video/coin/count")
    @ApiOperation(value = "查看视频投币数量", httpMethod = "GET")
    public JsonResponse<Map<String, Object>> getVideoCoinCount(@RequestParam Long videoId) {
        Long userId = null;
        try {
            userId = userSupport.getCurrentUserId();
        } catch (Exception ignored) {
        }
        Map<String, Object> result = videoService.getVideoCoinCount(videoId, userId);
        return new JsonResponse<>(result);
    }

    @PostMapping("/video/comment")
    @ApiOperation(value = "视频评论", httpMethod = "POST")
    public JsonResponse<String> addVideoComment(@RequestBody VideoComment videoComment) {
        Long userId = userSupport.getCurrentUserId();
        videoComment.setUserId(userId);
        videoService.addVideoComment(videoComment);
        return JsonResponse.success();
    }

    @GetMapping("/page/video/comment")
    @ApiOperation(value = "分页查询视频评论", httpMethod = "GET")
    public JsonResponse<PageResult<VideoComment>> pageListVideoComment(
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam("pageNum") Long videoId
    ) {
        PageResult<VideoComment> result = videoService.pageListVideoComment(pageSize, pageNum, videoId);
        return new JsonResponse<>(result);
    }

    @GetMapping("get/video/detail")
    @ApiOperation(value = "查看视频详情", httpMethod = "GET")
    public JsonResponse<Map<String, Object>> getVideoDetail(@RequestParam Long videoId) {
        Map<String, Object> result = videoService.getVideoDetail(videoId);
        return new JsonResponse<>(result);
    }
}
