package com.imooc.bilibili.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.domain.video.UserCoin;
import com.imooc.bilibili.exception.ConditionException;
import com.imooc.bilibili.mapper.UserCoinMapper;
import com.imooc.bilibili.service.UserCoinService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserCoinServiceImpl extends ServiceImpl<UserCoinMapper, UserCoin> implements UserCoinService {
    @Resource
    private UserCoinMapper userCoinMapper;

    @Override
    public Integer getUserCoinAmountByUserId(Long userId) {
        LambdaQueryWrapper<UserCoin> queryWrapper = new LambdaQueryWrapper<UserCoin>()
                .eq(UserCoin::getUserId, userId);
        UserCoin userCoin = userCoinMapper.selectOne(queryWrapper);
        if (ObjectUtil.isNull(userCoin)) {
            throw new ConditionException("没有该用户的硬币信息,userId=" + userId);
        }
        return userCoin.getAmount();
    }

    @Override
    public void addUserCoin(UserCoin userCoin) {
        userCoinMapper.insert(userCoin);
    }

    @Override
    public void updateUserCoinAmountByUserId(Long userId, Integer amount) {
        UserCoin userCoin = new UserCoin();
        userCoin.setUserId(userId);
        userCoin.setAmount(amount);
        userCoin.setUpdateTime(new Date());
        LambdaUpdateWrapper<UserCoin> updateWrapper = new LambdaUpdateWrapper<UserCoin>()
                .set(UserCoin::getAmount, amount)
                .set(UserCoin::getUpdateTime, new Date())
                .eq(UserCoin::getUserId, userId);
        userCoinMapper.update(userCoin, updateWrapper);
    }
}
