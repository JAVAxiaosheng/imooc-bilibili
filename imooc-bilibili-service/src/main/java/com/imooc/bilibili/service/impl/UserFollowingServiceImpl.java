package com.imooc.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.constant.UserConstant;
import com.imooc.bilibili.domain.FollowingGroup;
import com.imooc.bilibili.domain.User;
import com.imooc.bilibili.domain.UserFollowing;
import com.imooc.bilibili.domain.UserInfo;
import com.imooc.bilibili.exception.ConditionException;
import com.imooc.bilibili.mapper.UserFollowingMapper;
import com.imooc.bilibili.service.FollowingGroupService;
import com.imooc.bilibili.service.UserFollowingService;
import com.imooc.bilibili.service.UserInfoService;
import com.imooc.bilibili.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserFollowingServiceImpl extends ServiceImpl<UserFollowingMapper, UserFollowing> implements UserFollowingService {
    @Resource
    private UserFollowingMapper userFollowingMapper;

    @Resource
    private FollowingGroupService followingGroupService;

    @Resource
    private UserService userService;

    @Resource
    private UserInfoService userInfoService;


    @Override
    @Transactional
    public void addUserFollowings(UserFollowing userFollowing) {
        Long groupId = userFollowing.getGroupId();
        if (groupId == null) {
            FollowingGroup followingGroup = followingGroupService.getByType(UserConstant.USER_FOLLOWING_GROUP_TYPE_DEFAULT);
            userFollowing.setGroupId(followingGroup.getId());
        } else {
            FollowingGroup followingGroup = followingGroupService.getById(groupId);
            if (followingGroup == null) {
                throw new ConditionException("关注分组不存在!");
            }
        }
        Long followingId = userFollowing.getFollowingId();
        User user = userService.getById(followingId);
        if (user == null) {
            throw new ConditionException("关注的用户不存在!");
        }
        LambdaQueryWrapper<UserFollowing> queryWrapper = new LambdaQueryWrapper<UserFollowing>()
                .eq(UserFollowing::getUserId, user.getId())
                .eq(UserFollowing::getFollowingId, followingId);
        userFollowingMapper.delete(queryWrapper);
        userFollowing.setCreateTime(new Date());
        userFollowingMapper.insert(userFollowing);
    }

    // 获取用户关注列表
    @Override
    public List<FollowingGroup> getUserFollowings(Long userId) {
        LambdaQueryWrapper<UserFollowing> queryWrapper = new LambdaQueryWrapper<UserFollowing>()
                .eq(UserFollowing::getUserId, userId);
        List<UserFollowing> list = userFollowingMapper.selectList(queryWrapper);
        Set<Long> followingIdSet = list.stream().map(UserFollowing::getFollowingId).collect(Collectors.toSet());
        List<UserInfo> userInfoList = new ArrayList<>();
        if (followingIdSet.size() > 0) {
            LambdaQueryWrapper<UserInfo> userInfoQueryWrapper = new LambdaQueryWrapper<UserInfo>()
                    .in(UserInfo::getUserId, followingIdSet);
            userInfoList = userInfoService.list(userInfoQueryWrapper);
        }
        for (UserFollowing userFollowing : list) {
            for (UserInfo userInfo : userInfoList) {
                if (userFollowing.getFollowingId().equals(userInfo.getUserId())) {
                    userFollowing.setUserInfo(userInfo);
                }
            }
        }
        LambdaQueryWrapper<FollowingGroup> groupQueryWrapper = new LambdaQueryWrapper<FollowingGroup>()
                .eq(FollowingGroup::getUserId, userId)
                .or()
                .in(FollowingGroup::getType, "0", "1", "2");
        List<FollowingGroup> groupList = followingGroupService.list(groupQueryWrapper);
        FollowingGroup allGroup = new FollowingGroup();
        allGroup.setName(UserConstant.USER_FOLLOWING_GROUP_ALL_NAME);
        allGroup.setFollowingUserInfoInfoList(userInfoList);
        List<FollowingGroup> result = new ArrayList<>();
        result.add(allGroup);
        for (FollowingGroup group : groupList) {
            List<UserInfo> infoList = new ArrayList<>();
            for (UserFollowing userFollowing : list) {
                if (group.getId().equals(userFollowing.getGroupId())) {
                    infoList.add(userFollowing.getUserInfo());
                }
            }
            group.setFollowingUserInfoInfoList(infoList);
            result.add(group);
        }
        return result;
    }

    // 获取用户粉丝
    @Override
    public List<UserFollowing> getUserFans(Long userId) {
        LambdaQueryWrapper<UserFollowing> queryWrapper = new LambdaQueryWrapper<UserFollowing>()
                .eq(UserFollowing::getFollowingId, userId);
        List<UserFollowing> fanList = userFollowingMapper.selectList(queryWrapper);
        Set<Long> fanIdSet = fanList.stream().map(UserFollowing::getUserId).collect(Collectors.toSet());
        List<UserInfo> userInfoList = new ArrayList<>();
        if (fanIdSet.size() > 0) {
            LambdaQueryWrapper<UserInfo> userInfoQueryWrapper = new LambdaQueryWrapper<UserInfo>()
                    .in(UserInfo::getUserId, fanIdSet);
            userInfoList = userInfoService.list(userInfoQueryWrapper);
        }
        LambdaQueryWrapper<UserFollowing> userFollowingQueryWrapper = new LambdaQueryWrapper<UserFollowing>()
                .eq(UserFollowing::getUserId, userId);
        List<UserFollowing> followingList = userFollowingMapper.selectList(userFollowingQueryWrapper);
        for (UserFollowing fan : fanList) {
            for (UserInfo userInfo : userInfoList) {
                if (fan.getUserId().equals(userInfo.getUserId())) {
                    userInfo.setFollowed(false);
                    fan.setUserInfo(userInfo);
                }
            }
            for (UserFollowing userFollowing : followingList) {
                if (userFollowing.getFollowingId().equals(fan.getUserId())) {
                    fan.getUserInfo().setFollowed(true);
                }
            }
        }
        return fanList;
    }

    @Override
    public Long addFollowingGroup(FollowingGroup followingGroup) {
        followingGroup.setCreateTime(new Date());
        followingGroup.setType(UserConstant.USER_FOLLOWING_GROUP_TYPE_USER);
        followingGroupService.save(followingGroup);
        return followingGroup.getId();
    }

    @Override
    public List<FollowingGroup> getFollowingGroups(Long userId) {
        LambdaQueryWrapper<FollowingGroup> queryWrapper = new LambdaQueryWrapper<FollowingGroup>()
                .eq(FollowingGroup::getUserId, userId);
        return followingGroupService.list(queryWrapper);
    }

    @Override
    public List<UserInfo> checkFollowingStatus(List<UserInfo> userInfoList, Long userId) {
        LambdaQueryWrapper<UserFollowing> queryWrapper = new LambdaQueryWrapper<UserFollowing>()
                .eq(UserFollowing::getUserId, userId);
        List<UserFollowing> userFollowingList = userFollowingMapper.selectList(queryWrapper);
        for (UserInfo userInfo : userInfoList) {
            userInfo.setFollowed(false);
            for (UserFollowing userFollowing : userFollowingList) {
                if (userFollowing.getFollowingId().equals(userInfo.getUserId())) {
                    userInfo.setFollowed(true);
                }
            }
        }
        return userInfoList;
    }
}
