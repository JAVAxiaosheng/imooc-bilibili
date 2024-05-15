package com.imooc.bilibili.controller;

import com.imooc.bilibili.domain.JsonResponse;
import com.imooc.bilibili.domain.video.Danmu;
import com.imooc.bilibili.service.DanmuService;
import com.imooc.bilibili.support.UserSupport;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/danmu")
public class DanmuController {

    @Resource
    private UserSupport userSupport;

    @Resource
    private DanmuService danmuService;

    @GetMapping("/get/danmus")
    @ApiOperation(value = "查询弹幕", httpMethod = "GET")
    public JsonResponse<List<Danmu>> getDanmus(@RequestParam("videoId") Long videoId,
                                               String startTime,
                                               String endTime) throws Exception {
        List<Danmu> result = new ArrayList<>();
        try {
            userSupport.getCurrentUserId();
            result = danmuService.getDanmus(videoId, startTime, endTime);
        } catch (Exception ignored) {
            // 不登录不可以筛选时间段
            result = danmuService.getDanmus(videoId, null, null);
        }
        return new JsonResponse<>(result);
    }
}
