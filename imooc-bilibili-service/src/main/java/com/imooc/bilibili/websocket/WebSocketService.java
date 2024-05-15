package com.imooc.bilibili.websocket;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.imooc.bilibili.constant.UserMomentsConstant;
import com.imooc.bilibili.domain.video.Danmu;
import com.imooc.bilibili.service.DanmuService;
import com.imooc.bilibili.util.RocketMQUtil;
import com.imooc.bilibili.util.TokenUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.ApplicationContext;


// 通信路径,访问的时候使用
@ServerEndpoint("/lz/websocket/{token}")
@Slf4j
@Component
@Data
public class WebSocketService {

    // 获取当前客户端数量,连接数
    // AtomicInteger线程安全,原子操作
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

    // 连接一次就需要重新生成一个WebSocketService,多例模式
    // 线程安全
    public static final ConcurrentHashMap<String, WebSocketService> WEB_SOCKET_MAP = new ConcurrentHashMap<>();

    // 保持会话验证
    private Session session;

    private String sessionId;

    private Long userId;

    // 解决多例模式下的依赖注入问题
    private static ApplicationContext APPLICATION_CONTEXT;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketService.APPLICATION_CONTEXT = applicationContext;
    }

    // 打开websocket连接
    @OnOpen
    public void openConnection(Session session, @PathParam("token") String token) {
        try {
            this.userId = TokenUtil.verifyToken(token);
        } catch (Exception ignored) {
        }
        this.sessionId = session.getId();
        this.session = session;
        if (WEB_SOCKET_MAP.containsKey(sessionId)) {
            WEB_SOCKET_MAP.remove(sessionId);
            WEB_SOCKET_MAP.put(sessionId, this);
        } else {
            WEB_SOCKET_MAP.put(sessionId, this);
            // 获得连接之后在线客户端数量+1
            ONLINE_COUNT.getAndIncrement();
        }
        log.info("用户连接成功sessionId：{},当前在线人数为:{}", sessionId, ONLINE_COUNT.get());
        try {
            this.sendMessage("0");
        } catch (Exception e) {
            log.error("连接异常", e);
        }
    }

    // 关闭连接
    @OnClose
    public void closeConnection() {
        WEB_SOCKET_MAP.remove(sessionId);
        ONLINE_COUNT.getAndDecrement();
        log.info("用户退出成功sessionId：{},当前在线人数为:{}", sessionId, ONLINE_COUNT.get());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("用户信息sessionId:{},报文:{}", sessionId, message);
        if (!StrUtil.isEmptyOrUndefined(message)) {
            try {
                // 群发消息
                for (Map.Entry<String, WebSocketService> entry : WEB_SOCKET_MAP.entrySet()) {
                    WebSocketService webSocketService = entry.getValue();
                    // 先把对应的消息放到MQ中,排队
                    DefaultMQProducer danmuProducer = (DefaultMQProducer) WebSocketService.APPLICATION_CONTEXT.getBean("danmuProducer");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("sessionId", webSocketService.getSessionId());
                    jsonObject.put("message", message);
                    Message msg = new Message(UserMomentsConstant.TOPIC_DANMU, jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8));
                    RocketMQUtil.asyncSendMsg(danmuProducer, msg);
                }
                if (ObjectUtil.isNotNull(this.userId)) {
                    Danmu danmu = new Danmu();
                    danmu.setContent(message);
                    danmu.setDanmuTime(String.valueOf(new Date().getTime()));
                    danmu.setUserId(this.userId);
                    danmu.setCreateTime(new Date());
                    DanmuService danmuService = (DanmuService) WebSocketService.APPLICATION_CONTEXT.getBean("danmuServiceImpl");
                    danmuService.asyncAddDanmu(danmu);
                    danmuService.addDanmuToRedis(danmu);
                }
            } catch (Exception e) {
                log.error("弹幕群发异常:", e);
            }
        }
    }

    @OnError
    public void OnError(Throwable error) {
    }

    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("消息推送失败sendText:", e);
        }

    }
}
