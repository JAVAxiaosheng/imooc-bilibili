package com.imooc.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.domain.auth.AuthRole;
import com.imooc.bilibili.mapper.AuthRoleMapper;
import com.imooc.bilibili.service.AuthRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AuthRoleServiceImpl extends ServiceImpl<AuthRoleMapper, AuthRole> implements AuthRoleService {
    @Resource
    private AuthRoleMapper authRoleMapper;
}
