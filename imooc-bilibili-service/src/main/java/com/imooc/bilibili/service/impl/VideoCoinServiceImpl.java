package com.imooc.bilibili.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.domain.video.VideoCoin;
import com.imooc.bilibili.exception.ConditionException;
import com.imooc.bilibili.mapper.VideoCoinMapper;
import com.imooc.bilibili.service.VideoCoinService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class VideoCoinServiceImpl extends ServiceImpl<VideoCoinMapper, VideoCoin> implements VideoCoinService {
    @Resource
    private VideoCoinMapper videoCoinMapper;

    @Override
    public void addVideoCoin(VideoCoin videoCoin) {
        videoCoinMapper.insert(videoCoin);
    }

    @Override
    public VideoCoin getVideoCoinByVideoIdAndUserId(Long videoId, Long userId) {
        LambdaQueryWrapper<VideoCoin> queryWrapper = new LambdaQueryWrapper<VideoCoin>()
                .eq(VideoCoin::getVideoId, videoId)
                .eq(VideoCoin::getUserId, userId);
        return videoCoinMapper.selectOne(queryWrapper);
    }

    @Override
    public void updateVideoCoinById(VideoCoin videoCoin) {
        Long id = videoCoin.getId();
        if (ObjectUtil.isNull(id)) {
            throw new ConditionException("修改视频投币id为空");
        }
        LambdaUpdateWrapper<VideoCoin> updateWrapper = new LambdaUpdateWrapper<VideoCoin>()
                .set(VideoCoin::getAmount, videoCoin.getAmount())
                .set(VideoCoin::getUpdateTime, videoCoin.getUpdateTime())
                .eq(VideoCoin::getId, id);
        videoCoinMapper.update(videoCoin, updateWrapper);
    }

    @Override
    public Long getVideoCoinAmountByVideoId(Long videoId) {
        return videoCoinMapper.getVideoCoinAmountByVideoId(videoId);
    }
}
