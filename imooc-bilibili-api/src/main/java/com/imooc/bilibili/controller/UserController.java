package com.imooc.bilibili.controller;

import com.imooc.bilibili.domain.JsonResponse;
import com.imooc.bilibili.domain.User;
import com.imooc.bilibili.domain.UserInfo;
import com.imooc.bilibili.service.UserService;
import com.imooc.bilibili.support.UserSupport;
import com.imooc.bilibili.util.RSAUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
@Api(tags = "用户信息", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private UserSupport userSupport;

    @GetMapping("/rsa-pks")
    @ApiOperation(value = "获取公钥", httpMethod = "GET")
    public JsonResponse<String> getRsaPublicKey() {
        String pk = RSAUtil.getPublicKeyStr();
        return new JsonResponse<>(pk);
    }

    @PostMapping("/add")
    @ApiOperation(value = "注册", httpMethod = "POST")
    public JsonResponse<String> addUser(@RequestBody User user) {
        userService.addUser(user);
        return JsonResponse.success();
    }

    @PostMapping("/tokens")
    @ApiOperation(value = "登录", httpMethod = "POST")
    public JsonResponse<String> login(@RequestBody User user) throws Exception {
        String token = userService.login(user);
        return new JsonResponse<>(token);
    }

    @GetMapping("/info")
    @ApiOperation(value = "获取用户信息", httpMethod = "GET")
    public JsonResponse<User> getUserInfo() {
        Long userId = userSupport.getCurrentUserId();
        User user = userService.getInfoById(userId);
        return new JsonResponse<>(user);
    }

    @PutMapping("/update")
    @ApiOperation(value = "更新用户信息", httpMethod = "PUT")
    public JsonResponse<String> updateUserInfo(@RequestBody User user) throws Exception {
        Long userId = userSupport.getCurrentUserId();
        user.setId(userId);
        userService.updateUserInfo(user);
        return JsonResponse.success();
    }

    @PutMapping("/update/info")
    @ApiOperation(value = "更新用户信息2", httpMethod = "PUT")
    public JsonResponse<String> updateUserInfos(@RequestBody UserInfo userInfo) throws Exception {
        Long userId = userSupport.getCurrentUserId();
        userInfo.setUserId(userId);
        userService.updateUserInfos(userInfo);
        return JsonResponse.success();
    }

}
