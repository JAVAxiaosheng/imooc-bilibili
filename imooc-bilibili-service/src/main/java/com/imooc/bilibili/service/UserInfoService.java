package com.imooc.bilibili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.bilibili.domain.UserInfo;

public interface UserInfoService extends IService<UserInfo> {
    Integer getOneInfo(Integer id);
}
