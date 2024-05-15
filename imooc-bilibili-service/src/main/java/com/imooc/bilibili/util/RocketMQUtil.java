package com.imooc.bilibili.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.message.Message;

import java.util.concurrent.TimeUnit;

@Slf4j
public class RocketMQUtil {
    // 同步发送信息
    public static void syncSendMsg(DefaultMQProducer producer, Message msg) throws Exception {
        SendResult result = producer.send(msg);
        System.out.println(result);
    }

    // 异步发送信息
    public static void asyncSendMsg2(DefaultMQProducer producer, Message msg) throws Exception {
        int msgCount = 2;
        CountDownLatch2 countDownLatch = new CountDownLatch2(msgCount);
        for (int i = 0; i < msgCount; i++) {
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    countDownLatch.countDown();
                    System.out.println(sendResult.getMsgId());
                }

                @Override
                public void onException(Throwable throwable) {
                    countDownLatch.countDown();
                    log.error("发送消息异常：", throwable);
                }
            });
        }
        countDownLatch.await(5, TimeUnit.SECONDS);
    }

    public static void asyncSendMsg(DefaultMQProducer producer, Message msg) throws Exception {
        producer.send(msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("异步发送消息成功，消息id：{}", sendResult.getMsgId());
            }

            @Override
            public void onException(Throwable e) {
                log.error("异步发送消息失败", e);
            }
        });
    }
}
