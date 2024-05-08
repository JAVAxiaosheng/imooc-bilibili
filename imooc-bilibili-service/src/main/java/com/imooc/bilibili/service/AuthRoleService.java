package com.imooc.bilibili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.bilibili.domain.auth.AuthRole;
import com.imooc.bilibili.domain.auth.AuthRoleElementOperation;
import com.imooc.bilibili.domain.auth.AuthRoleMenu;

import java.util.List;
import java.util.Set;

public interface AuthRoleService extends IService<AuthRole> {
    List<AuthRoleElementOperation> getAuthRoleElementOperationsByRoleIds(Set<Long> roleIds);

    List<AuthRoleMenu> getAuthRoleMenusByRoleIds(Set<Long> roleIds);
}
