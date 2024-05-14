package com.imooc.bilibili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.bilibili.domain.UserInfo;

import java.util.List;
import java.util.Set;

public interface UserInfoService extends IService<UserInfo> {
    Integer getOneInfo(Integer id);

    List<UserInfo> batchGetUserInfoByUserIds(Set<Long> userIdList);
}
