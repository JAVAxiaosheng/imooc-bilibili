package com.imooc.bilibili.websocket;

import javax.websocket.Session;

public interface WebSocketService {
    void openConnection(Session session);
    void closeConnection();
    void onMessage(String message);
    void OnError(Throwable error);
    void sendMessage(String message) throws Exception;
}
