package com.imooc.bilibili.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.bilibili.constant.UserMomentsConstant;
import com.imooc.bilibili.domain.UserMoments;
import com.imooc.bilibili.mapper.UserMomentsMapper;
import com.imooc.bilibili.service.UserMomentsService;
import com.imooc.bilibili.util.RocketMQUtil;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class UserMomentsServiceImpl extends ServiceImpl<UserMomentsMapper, UserMoments> implements UserMomentsService {
    @Resource
    private UserMomentsMapper userMomentsMapper;

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void addUserMoments(UserMoments userMoments) throws Exception {
        userMoments.setCreateTime(new Date());
        userMomentsMapper.insert(userMoments);
        // 向MQ中发送消息
        DefaultMQProducer producer = (DefaultMQProducer) applicationContext.getBean("momentsProducer");
        Message msg = new Message(UserMomentsConstant.TOPIC_MOMENTS, JSONObject.toJSONString(userMoments).getBytes(StandardCharsets.UTF_8));
        RocketMQUtil.syncSendMsg(producer, msg);
    }

    @Override
    public List<UserMoments> getUserSubscribedMoments(Long userId) {
        String key = UserMomentsConstant.SUBSCRIBED_REDIS_KEY_PRE + userId;
        String listStr = redisTemplate.opsForValue().get(key);
        return JSONArray.parseArray(listStr, UserMoments.class);
    }
}
