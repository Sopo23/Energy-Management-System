package com.example.demo.config;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;

public class NotificationWebSocketHandler extends TextWebSocketHandler {

    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            String query = session.getUri().getQuery();
            String username = query.split("=")[1];

            sessions.put(username, session);
            System.out.println("WebSocket connection established for user: " + username);
        } catch (Exception e) {
            System.err.println("Failed to establish WebSocket connection: " + e.getMessage());
            try {
                session.close();
            } catch (Exception ignored) {}
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println("Received message: " + message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.values().remove(session);
        System.out.println("WebSocket connection closed.");
    }

    public void sendNotification(String username, String notification) {
        WebSocketSession session = sessions.get(username);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(notification));
            } catch (Exception e) {
                System.err.println("Failed to send notification: " + e.getMessage());
            }
        }
    }
}
