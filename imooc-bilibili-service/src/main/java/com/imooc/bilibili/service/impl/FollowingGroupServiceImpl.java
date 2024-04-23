package com.imooc.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.domain.FollowingGroup;
import com.imooc.bilibili.mapper.FollowingGroupMapper;
import com.imooc.bilibili.service.FollowingGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FollowingGroupServiceImpl extends ServiceImpl<FollowingGroupMapper, FollowingGroup> implements FollowingGroupService {
    @Resource
    private FollowingGroupMapper followingGroupMapper;

    @Override
    public FollowingGroup getByType(String type) {
        LambdaQueryWrapper<FollowingGroup> queryWrapper = new LambdaQueryWrapper<FollowingGroup>()
                .eq(FollowingGroup::getType, type);
        return followingGroupMapper.selectOne(queryWrapper);
    }

    @Override
    public FollowingGroup getById(Long id) {
        return followingGroupMapper.selectById(id);
    }
}
