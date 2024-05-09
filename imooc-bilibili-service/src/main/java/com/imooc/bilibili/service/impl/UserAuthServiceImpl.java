package com.imooc.bilibili.service.impl;

import com.imooc.bilibili.constant.AuthRoleConstant;
import com.imooc.bilibili.domain.auth.*;
import com.imooc.bilibili.service.AuthRoleService;
import com.imooc.bilibili.service.UserAuthService;
import com.imooc.bilibili.service.UserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private AuthRoleService authRoleService;

    @Override
    public UserAuthorities getUserAuthorities(Long userId) {
        List<UserRole> userRoleList = userRoleService.getUserRolesByUserId(userId);
        Set<Long> roleIdSet = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
        List<AuthRoleElementOperation> roleElementOperationList = authRoleService.getAuthRoleElementOperationsByRoleIds(roleIdSet);
        List<AuthRoleMenu> authRoleMenuList = authRoleService.getAuthRoleMenusByRoleIds(roleIdSet);
        UserAuthorities userAuthorities = new UserAuthorities();
        userAuthorities.setRoleElementOperationList(roleElementOperationList);
        userAuthorities.setRoleMenuList(authRoleMenuList);
        return userAuthorities;
    }

    @Override
    public void addUserDefaultRole(Long id) {
        UserRole userRole = new UserRole();
        AuthRole role = authRoleService.getRoleByCode(AuthRoleConstant.ROLE_CODE_LV0);
        userRole.setUserId(id);
        userRole.setRoleId(role.getId());
        userRoleService.addUserRole(userRole);
    }
}
