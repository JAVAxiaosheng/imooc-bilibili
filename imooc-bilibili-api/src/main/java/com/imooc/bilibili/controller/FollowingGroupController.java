package com.imooc.bilibili.controller;

import com.imooc.bilibili.service.FollowingGroupService;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/following/group")
@Api(tags = "用户分组关注信息", produces = MediaType.APPLICATION_JSON_VALUE)
public class FollowingGroupController {
    @Resource
    private FollowingGroupService followingGroupService;
}
