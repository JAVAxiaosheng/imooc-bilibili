package com.imooc.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.domain.auth.AuthRoleElementOperation;
import com.imooc.bilibili.mapper.AuthRoleElementOperationMapper;
import com.imooc.bilibili.service.AuthRoleElementOperationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AuthRoleElementOperationServiceImpl extends ServiceImpl<AuthRoleElementOperationMapper, AuthRoleElementOperation> implements AuthRoleElementOperationService {
    @Resource
    private AuthRoleElementOperationMapper authRoleElementOperationMapper;
}
