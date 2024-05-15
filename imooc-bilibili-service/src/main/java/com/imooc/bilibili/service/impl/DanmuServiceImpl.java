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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class DanmuServiceImpl extends ServiceImpl<DanmuMapper, Danmu> implements DanmuService {
    private static final String DANMU_KEY = "dm-video-";

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
    public List<Danmu> getDanmus(Long videoId, String startTime, String endTime) throws Exception {
        String key = DANMU_KEY + videoId;
        String value = redisTemplate.opsForValue().get(key);
        List<Danmu> list = new ArrayList<>();
        if (!StrUtil.isEmptyOrUndefined(value)) {
            // 查缓存
            list = JSONObject.parseArray(value, Danmu.class);
            if (!StrUtil.isEmptyOrUndefined(startTime) && !StrUtil.isEmptyOrUndefined(endTime)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date startDate = sdf.parse(startTime);
                Date endDate = sdf.parse(endTime);
                List<Danmu> childList = new ArrayList<>();
                for (Danmu danmu : list) {
                    Date createTime = danmu.getCreateTime();
                    if (createTime.after(startDate) && createTime.before(endDate)) {
                        childList.add(danmu);
                    }
                }
                list = childList;
            }
        } else {
            // 查数据库
            LambdaQueryWrapper<Danmu> queryWrapper = new LambdaQueryWrapper<Danmu>()
                    .eq(Danmu::getVideoId, videoId)
                    .between((!StrUtil.isEmptyOrUndefined(startTime) && !StrUtil.isEmptyOrUndefined(endTime)),
                            Danmu::getCreateTime, startTime, endTime);
            list = danmuMapper.selectList(queryWrapper);
            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(list));
        }
        return list;
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
