package com.imooc.bilibili.websocket.impl;

import com.imooc.bilibili.websocket.WebSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
// 通信路径,访问的时候使用
@ServerEndpoint("/lz/websocket")
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {

    // 获取当前客户端数量,连接数
    // AtomicInteger线程安全,原子操作
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

    // 连接一次就需要重新生成一个WebSocketService,多例模式
    // 线程安全
    private static final ConcurrentHashMap<String, WebSocketService> WEB_SOCKET_MAP = new ConcurrentHashMap<>();

    // 保持会话验证
    private Session session;

    private String sessionId;

    // 打开websocket连接
    @OnOpen
    @Override
    public void openConnection(Session session) {
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
    @Override
    public void closeConnection() {
        WEB_SOCKET_MAP.remove(sessionId);
        ONLINE_COUNT.getAndDecrement();
        log.info("用户退出成功：{},当前在线人数为:{}", sessionId, ONLINE_COUNT.get());
    }

    @OnMessage
    @Override
    public void onMessage(String message) {
    }

    @OnError
    @Override
    public void OnError(Throwable error) {
    }

    @Override
    public void sendMessage(String message) throws Exception {
        this.session.getBasicRemote().sendText(message);
    }
}
