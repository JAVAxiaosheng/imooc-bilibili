package com.imooc.bilibili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.bilibili.domain.RefreshToken;

public interface RefreshTokenService extends IService<RefreshToken> {
    Integer deleteRefreshToken(String refreshToken, Long userId);

    Integer addRefreshToken(String refreshToken, Long userId);

    RefreshToken getRefreshTokenDetail(String refreshToken);
}
