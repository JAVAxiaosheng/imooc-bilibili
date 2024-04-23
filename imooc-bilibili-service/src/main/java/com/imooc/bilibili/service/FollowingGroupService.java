package com.imooc.bilibili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.bilibili.domain.FollowingGroup;


public interface FollowingGroupService extends IService<FollowingGroup> {
    FollowingGroup getByType(String type);

    FollowingGroup getById(Long id);
}
