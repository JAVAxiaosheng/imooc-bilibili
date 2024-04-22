package com.imooc.bilibili.controller;

import com.imooc.bilibili.service.UserInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user/info")
// http://localhost:8060/user/info/get/info?id=24
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    @GetMapping("/get/info")
    public Integer getInfoOne(@RequestParam("id") Integer id) {
        return userInfoService.getOneInfo(id);
    }
}
