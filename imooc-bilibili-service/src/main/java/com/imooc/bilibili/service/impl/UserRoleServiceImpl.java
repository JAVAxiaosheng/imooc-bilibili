package com.imooc.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.domain.auth.UserRole;
import com.imooc.bilibili.mapper.UserRoleMapper;
import com.imooc.bilibili.service.UserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
    @Resource
    private UserRoleMapper userRoleMapper;


    @Override
    public List<UserRole> getUserRolesByUserId(Long userId) {
        return userRoleMapper.getUserRolesByUserId(userId);
    }

    @Override
    public void addUserRole(UserRole userRole) {
        userRole.setCreateTime(new Date());
        userRoleMapper.insert(userRole);
    }
}
