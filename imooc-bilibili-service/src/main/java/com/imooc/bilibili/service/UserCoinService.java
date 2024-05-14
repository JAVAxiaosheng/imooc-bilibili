package com.imooc.bilibili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.bilibili.domain.video.UserCoin;

public interface UserCoinService extends IService<UserCoin> {
    Integer getUserCoinAmountByUserId(Long userId);

    void addUserCoin(UserCoin userCoin);

    void updateUserCoinAmountByUserId(Long userId, Integer amount);
}
