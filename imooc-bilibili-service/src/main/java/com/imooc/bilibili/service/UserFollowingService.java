package com.imooc.bilibili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.bilibili.domain.FollowingGroup;
import com.imooc.bilibili.domain.UserFollowing;
import com.imooc.bilibili.domain.UserInfo;

import java.util.List;


public interface UserFollowingService extends IService<UserFollowing> {
    void addUserFollowings(UserFollowing userFollowing);

    List<FollowingGroup> getUserFollowings(Long userId);

    // 获取用户粉丝
    List<UserFollowing> getUserFans(Long userId);

    Long addFollowingGroup(FollowingGroup followingGroup);

    List<FollowingGroup> getFollowingGroups(Long userId);

    List<UserInfo> checkFollowingStatus(List<UserInfo> userInfoList, Long userId);
}
