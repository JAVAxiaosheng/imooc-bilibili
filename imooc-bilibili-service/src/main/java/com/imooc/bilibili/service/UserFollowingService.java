package com.imooc.bilibili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.bilibili.domain.FollowingGroup;
import com.imooc.bilibili.domain.UserFollowing;

import java.util.List;


public interface UserFollowingService extends IService<UserFollowing> {
    void addUserFollowings(UserFollowing userFollowing);

    List<FollowingGroup> getUserFollowings(Long userId);

    // 获取用户粉丝
    List<UserFollowing> getUserFans(Long userId);
}
