package com.imooc.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.domain.UserInfo;
import com.imooc.bilibili.mapper.UserInfoMapper;
import com.imooc.bilibili.service.UserInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public Integer getOneInfo(Integer id) {
        return userInfoMapper.getOneInfoById(id);
    }
}
