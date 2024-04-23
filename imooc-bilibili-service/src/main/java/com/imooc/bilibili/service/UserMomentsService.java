package com.imooc.bilibili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.bilibili.domain.UserMoments;

import java.util.List;

public interface UserMomentsService extends IService<UserMoments> {
    void addUserMoments(UserMoments userMoments) throws Exception;

    List<UserMoments> getUserSubscribedMoments(Long userId);
}
