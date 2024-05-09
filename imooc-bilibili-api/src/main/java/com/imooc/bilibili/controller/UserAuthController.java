package com.imooc.bilibili.controller;

import com.imooc.bilibili.annotation.ApiLimitedRole;
import com.imooc.bilibili.constant.AuthRoleConstant;
import com.imooc.bilibili.domain.JsonResponse;
import com.imooc.bilibili.domain.auth.UserAuthorities;
import com.imooc.bilibili.service.UserAuthService;
import com.imooc.bilibili.support.UserSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user/auth")
@Api(tags = "用户权限信息", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserAuthController {
    @Resource
    private UserSupport userSupport;

    @Resource
    private UserAuthService userAuthService;

    @GetMapping("/get/authorities")
    @ApiOperation(value = "获取用户权限", httpMethod = "GET")
    public JsonResponse<UserAuthorities> getUserAuthorities() {
        Long userId = userSupport.getCurrentUserId();
        UserAuthorities userAuthorities = userAuthService.getUserAuthorities(userId);
        return new JsonResponse<>(userAuthorities);
    }
}
