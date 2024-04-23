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
        return null;
    }
}
