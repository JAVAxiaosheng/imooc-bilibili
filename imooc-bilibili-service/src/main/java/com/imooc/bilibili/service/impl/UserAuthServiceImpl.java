package com.imooc.bilibili.service.impl;

import com.imooc.bilibili.domain.auth.UserAuthorities;
import com.imooc.bilibili.service.UserAuthService;
import org.springframework.stereotype.Service;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    @Override
    public UserAuthorities getUserAuthorities(Long userId) {
        return null;
    }
}
