package com.imooc.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.domain.auth.AuthRoleMenu;
import com.imooc.bilibili.mapper.AuthRoleMenuMapper;
import com.imooc.bilibili.service.AuthRoleMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AuthRoleMenuServiceImpl extends ServiceImpl<AuthRoleMenuMapper, AuthRoleMenu> implements AuthRoleMenuService {
    @Resource
    private AuthRoleMenuMapper authRoleMenuMapper;
}
