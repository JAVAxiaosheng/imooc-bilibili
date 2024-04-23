package com.imooc.bilibili.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.bilibili.domain.PageResult;
import com.imooc.bilibili.domain.User;
import com.imooc.bilibili.domain.UserInfo;

public interface UserService extends IService<User> {
    void addUser(User user);

    String login(User user) throws Exception;

    User getInfoById(Long id);

    void updateUserInfo(User user) throws Exception;

    void updateUserInfos(UserInfo userInfo);

    PageResult<UserInfo> getUserInfosOnPage(JSONObject params);
}
