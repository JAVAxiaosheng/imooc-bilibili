package com.imooc.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.domain.RefreshToken;
import com.imooc.bilibili.mapper.RefreshTokenMapper;
import com.imooc.bilibili.service.RefreshTokenService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class RefreshTokenServiceImpl extends ServiceImpl<RefreshTokenMapper, RefreshToken> implements RefreshTokenService {
    @Resource
    private RefreshTokenMapper refreshTokenMapper;

    @Override
    public Integer deleteRefreshToken(String refreshToken, Long userId) {
        LambdaQueryWrapper<RefreshToken> queryWrapper = new LambdaQueryWrapper<RefreshToken>()
                .eq(RefreshToken::getRefreshToken, refreshToken);
        return refreshTokenMapper.delete(queryWrapper);
    }

    @Override
    public Integer addRefreshToken(String refreshToken, Long userId) {
        RefreshToken rt = new RefreshToken();
        rt.setUserId(userId);
        rt.setRefreshToken(refreshToken);
        rt.setCreateTime(new Date());
        return refreshTokenMapper.insert(rt);
    }

    @Override
    public RefreshToken getRefreshTokenDetail(String refreshToken) {
        LambdaQueryWrapper<RefreshToken> queryWrapper = new LambdaQueryWrapper<RefreshToken>()
                .eq(RefreshToken::getRefreshToken, refreshToken);
        return refreshTokenMapper.selectOne(queryWrapper);
    }
}
