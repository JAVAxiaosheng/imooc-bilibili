package com.imooc.bilibili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.bilibili.domain.auth.UserRole;

import java.util.List;

public interface UserRoleService extends IService<UserRole> {
    List<UserRole> getUserRolesByUserId(Long userId);

    void addUserRole(UserRole userRole);
}
