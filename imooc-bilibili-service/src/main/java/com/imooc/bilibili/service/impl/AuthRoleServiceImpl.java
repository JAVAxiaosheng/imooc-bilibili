package com.imooc.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.domain.auth.AuthRole;
import com.imooc.bilibili.domain.auth.AuthRoleElementOperation;
import com.imooc.bilibili.domain.auth.AuthRoleMenu;
import com.imooc.bilibili.mapper.AuthRoleMapper;
import com.imooc.bilibili.service.AuthRoleElementOperationService;
import com.imooc.bilibili.service.AuthRoleMenuService;
import com.imooc.bilibili.service.AuthRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class AuthRoleServiceImpl extends ServiceImpl<AuthRoleMapper, AuthRole> implements AuthRoleService {
    @Resource
    private AuthRoleElementOperationService authRoleElementOperationService;

    @Resource
    private AuthRoleMenuService authRoleMenuService;

    @Resource
    private AuthRoleMapper authRoleMapper;

    @Override
    public List<AuthRoleElementOperation> getAuthRoleElementOperationsByRoleIds(Set<Long> roleIds) {
        return authRoleElementOperationService.getAuthRoleElementOperationsByRoleIds(roleIds);
    }

    @Override
    public List<AuthRoleMenu> getAuthRoleMenusByRoleIds(Set<Long> roleIds) {
        return authRoleMenuService.getAuthRoleMenusByRoleIds(roleIds);
    }

    @Override
    public AuthRole getRoleByCode(String roleCode) {
        LambdaQueryWrapper<AuthRole> queryWrapper = new LambdaQueryWrapper<AuthRole>()
                .eq(AuthRole::getCode, roleCode);
        return authRoleMapper.selectOne(queryWrapper);
    }
}
