package com.imooc.bilibili.controller;

import com.imooc.bilibili.annotation.ApiLimitedRole;
import com.imooc.bilibili.annotation.DataLimited;
import com.imooc.bilibili.constant.AuthRoleConstant;
import com.imooc.bilibili.domain.JsonResponse;
import com.imooc.bilibili.domain.UserMoments;
import com.imooc.bilibili.service.UserMomentsService;
import com.imooc.bilibili.support.UserSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user/moments")
@Api(tags = "用户动态信息", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserMomentsController {
    @Resource
    private UserMomentsService userMomentsService;

    @Resource
    private UserSupport userSupport;

    @ApiLimitedRole(limitedRoleCodeList = {AuthRoleConstant.ROLE_CODE_LV0})
    @DataLimited
    @PostMapping("/add/moments")
    @ApiOperation(value = "用户添加动态", httpMethod = "POST")
    public JsonResponse<String> addUserMoments(@RequestBody UserMoments userMoments) throws Exception {
        Long userId = userSupport.getCurrentUserId();
        userMoments.setUserId(userId);
        userMomentsService.addUserMoments(userMoments);
        return JsonResponse.success();
    }

    @GetMapping("/get/user/subscribed/moments")
    @ApiOperation(value = "获取用户订阅的动态", httpMethod = "GET")
    public JsonResponse<List<UserMoments>> getUserSubscribedMoments() {
        Long userId = userSupport.getCurrentUserId();
        List<UserMoments> result = userMomentsService.getUserSubscribedMoments(userId);
        return new JsonResponse<>(result);
    }

}
