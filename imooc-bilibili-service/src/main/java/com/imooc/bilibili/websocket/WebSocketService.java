package com.imooc.bilibili.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.ApplicationContext;


// 通信路径,访问的时候使用
@ServerEndpoint("/lz/websocket")
@Slf4j
@Component
public class WebSocketService {

    // 获取当前客户端数量,连接数
    // AtomicInteger线程安全,原子操作
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

    // 连接一次就需要重新生成一个WebSocketService,多例模式
    // 线程安全
    private static final ConcurrentHashMap<String, WebSocketService> WEB_SOCKET_MAP = new ConcurrentHashMap<>();

    // 保持会话验证
    private Session session;

    private String sessionId;

    // 解决多例模式下的依赖注入问题
    private static ApplicationContext APPLICATION_CONTEXT;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketService.APPLICATION_CONTEXT = applicationContext;
    }

    // 打开websocket连接
    @OnOpen
    public void openConnection(Session session) {
//        RedisTemplate<String, String> redisTemplate = (RedisTemplate) WebSocketService.APPLICATION_CONTEXT.getBean("redisTemplate");
//        redisTemplate.opsForValue().get("hasgjahsgdjhjsd");
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
        log.info("用户连接成功：{},当前在线人数为:{}", sessionId, ONLINE_COUNT.get());
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
        log.info("用户退出成功：{},当前在线人数为:{}", sessionId, ONLINE_COUNT.get());
    }

    @OnMessage
    public void onMessage(String message) {

    }

    @OnError
    public void OnError(Throwable error) {
    }

    public void sendMessage(String message) throws Exception {
        this.session.getBasicRemote().sendText(message);
    }
}
