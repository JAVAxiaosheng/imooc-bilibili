package com.imooc.bilibili.comfig;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.imooc.bilibili.constant.UserMomentsConstant;
import com.imooc.bilibili.domain.UserFollowing;
import com.imooc.bilibili.domain.UserMoments;
import com.imooc.bilibili.service.UserFollowingService;
import com.imooc.bilibili.websocket.WebSocketService;
import com.mysql.cj.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class RocketMQConfig {
    @Value("${rocketmq.name.server.address}")
    private String nameServerAddress;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private UserFollowingService userFollowingService;

    // 消息生产者
    @Bean("momentsProducer")
    public DefaultMQProducer momentsProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(UserMomentsConstant.GROUP_MOMENTS);
        producer.setNamesrvAddr(nameServerAddress);
        producer.start();
        return producer;
    }

    // 消息消费者
    @Bean("momentsConsumer")
    public DefaultMQPushConsumer momentsConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(UserMomentsConstant.GROUP_MOMENTS);
        consumer.setNamesrvAddr(nameServerAddress);
        // 订阅
        consumer.subscribe(UserMomentsConstant.TOPIC_MOMENTS, "*");
        // 添加监听器,监听消息抓取和推送
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            // 参数:消息和上下文
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                MessageExt messageExt = list.get(0);
                if (messageExt == null) {
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                String bodyStr = new String(messageExt.getBody());
                UserMoments userMoments = JSONObject.toJavaObject(JSONObject.parseObject(bodyStr), UserMoments.class);
                Long userId = userMoments.getUserId();
                List<UserFollowing> userFans = userFollowingService.getUserFans(userId);
                for (UserFollowing userFan : userFans) {
                    String key = UserMomentsConstant.SUBSCRIBED_REDIS_KEY_PRE + userFan.getUserId();
                    String subscribedListStr = redisTemplate.opsForValue().get(key);
                    List<UserMoments> subscribedList;
                    if (StringUtils.isNullOrEmpty(subscribedListStr)) {
                        subscribedList = new ArrayList<>();
                    } else {
                        subscribedList = JSONArray.parseArray(subscribedListStr, UserMoments.class);
                    }
                    subscribedList.add(userMoments);
                    redisTemplate.opsForValue().set(key, JSONObject.toJSONString(subscribedList));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        return consumer;
    }


    @Bean("danmuProducer")
    public DefaultMQProducer danmuProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(UserMomentsConstant.DANMU_GROUP);
        producer.setNamesrvAddr(nameServerAddress);
        producer.start();
        return producer;
    }

    // 消息消费者
    @Bean("danmuConsumer")
    public DefaultMQPushConsumer danmuConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(UserMomentsConstant.DANMU_GROUP);
        consumer.setNamesrvAddr(nameServerAddress);
        // 订阅
        consumer.subscribe(UserMomentsConstant.TOPIC_DANMU, "*");
        // 添加监听器,监听消息抓取和推送
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            // 参数:消息和上下文
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                MessageExt messageExt = list.get(0);
                if (messageExt == null) {
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                String bodyStr = new String(messageExt.getBody());
                JSONObject jsonObject = JSONObject.parseObject(bodyStr);
                String sessionId = jsonObject.getString("sessionId");
                String message = jsonObject.getString("message");
                WebSocketService webSocketService = WebSocketService.WEB_SOCKET_MAP.get(sessionId);
                if (webSocketService.getSession().isOpen()) {
                    try {
                        webSocketService.sendMessage(message);
                    } catch (Exception e) {
                        log.error("消息推送失败:", e);
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        return consumer;
    }


}
