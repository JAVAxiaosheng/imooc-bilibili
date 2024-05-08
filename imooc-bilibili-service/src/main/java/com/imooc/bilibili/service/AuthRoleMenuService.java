package com.imooc.bilibili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.bilibili.domain.auth.AuthRoleMenu;

import java.util.List;
import java.util.Set;

public interface AuthRoleMenuService extends IService<AuthRoleMenu> {
    List<AuthRoleMenu> getAuthRoleMenusByRoleIds(Set<Long> roleIds);
}
