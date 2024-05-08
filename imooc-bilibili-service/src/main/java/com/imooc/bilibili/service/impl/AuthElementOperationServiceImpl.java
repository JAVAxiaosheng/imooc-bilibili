package com.imooc.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.domain.auth.AuthElementOperation;
import com.imooc.bilibili.mapper.AuthElementOperationMapper;
import com.imooc.bilibili.service.AuthElementOperationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AuthElementOperationServiceImpl extends ServiceImpl<AuthElementOperationMapper, AuthElementOperation> implements AuthElementOperationService {
    @Resource
    private AuthElementOperationMapper authElementOperationMapper;
}
