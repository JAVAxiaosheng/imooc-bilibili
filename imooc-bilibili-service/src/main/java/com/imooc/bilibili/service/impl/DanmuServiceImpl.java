package com.imooc.bilibili.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.domain.video.Danmu;
import com.imooc.bilibili.exception.ConditionException;
import com.imooc.bilibili.mapper.DanmuMapper;
import com.imooc.bilibili.service.DanmuService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class DanmuServiceImpl extends ServiceImpl<DanmuMapper, Danmu> implements DanmuService {
    @Resource
    private DanmuMapper danmuMapper;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void addDanmu(Danmu danmu) {
        danmuMapper.insert(danmu);
    }

    @Override
    @Async
    public void asyncAddDanmu(Danmu danmu) {
        danmuMapper.insert(danmu);
    }

    @Override
    public List<Danmu> getDanmus(Map<String, Date> params) {
        Date startDate = params.get("startDate");
        Date endDate = params.get("endDate");
        if (ObjectUtil.isNull(startDate) || ObjectUtil.isNull(endDate)) {
            throw new ConditionException("开始||结束时间为空");
        }
        LambdaQueryWrapper<Danmu> queryWrapper = new LambdaQueryWrapper<Danmu>()
                .between(Danmu::getCreateTime, startDate, endDate);
        return danmuMapper.selectList(queryWrapper);
    }

    @Override
    public void addDanmuToRedis(Danmu danmu) {
        String key = "danmu-video" + danmu.getVideoId();
        String value = redisTemplate.opsForValue().get(key);
        List<Danmu> list = new ArrayList<>();
        if (!StrUtil.isEmptyOrUndefined(value)) {
            list = JSONArray.parseArray(value, Danmu.class);
        }
        list.add(danmu);
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(list));
    }
}
