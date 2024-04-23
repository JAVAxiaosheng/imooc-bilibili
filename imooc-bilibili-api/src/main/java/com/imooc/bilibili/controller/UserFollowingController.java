package com.imooc.bilibili.controller;

import com.imooc.bilibili.service.UserFollowingService;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user/following")
@Api(tags = "用户关注信息", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserFollowingController {
    @Resource
    private UserFollowingService userFollowingService;
}
