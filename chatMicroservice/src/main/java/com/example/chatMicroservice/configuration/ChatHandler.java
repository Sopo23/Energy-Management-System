package com.example.chatMicroservice.configuration;

import com.example.chatMicroservice.model.ChatMessage;
import com.example.chatMicroservice.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {

    private final Map<String, List<WebSocketSession>> chatRoomSessions = new ConcurrentHashMap<>();
    private final Map<String, Integer> chatRoomUsers = new ConcurrentHashMap<>();
    private final Map<String, List<ChatMessage>> userUnreadMessages = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String path = session.getUri().getPath();
        String chatRoomId = extractChatRoomId(path);
        System.out.println(chatRoomId);

        if (chatRoomId == null || chatRoomId.isEmpty()) {
            session.close(CloseStatus.BAD_DATA);
            return;
        }

        session.getAttributes().put("chatRoomId", chatRoomId);
        chatRoomSessions.computeIfAbsent(chatRoomId, k -> Collections.synchronizedList(new ArrayList<>())).add(session);

        int currentUsers = chatRoomSessions.get(chatRoomId).size();
        System.out.println("For the session: " + chatRoomId + " there are: " + currentUsers);
        chatRoomUsers.put(chatRoomId, currentUsers);

        if (currentUsers >= 2) {
            handleUnreadMessages(chatRoomId);
        }

        System.out.println("New connection established. ChatRoomId: " + chatRoomId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        ChatMessage incomingMessage = objectMapper.readValue(textMessage.getPayload(), ChatMessage.class);
        String chatRoomId = incomingMessage.getChatRoomId();

        if (chatRoomId == null || chatRoomId.isEmpty()) {
            return;
        }

        if (chatRoomId.equals("group1")) {
            handleGroupAnnouncement(incomingMessage);
        } else {
            handlePrivateChat(chatRoomId, incomingMessage, session);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String chatRoomId = (String) session.getAttributes().get("chatRoomId");

        if (chatRoomId != null) {
            List<WebSocketSession> sessions = chatRoomSessions.getOrDefault(chatRoomId, new ArrayList<>());
            sessions.remove(session);

            if (sessions.isEmpty()) {
                chatRoomSessions.remove(chatRoomId);
                chatRoomUsers.remove(chatRoomId);
            } else {
                chatRoomUsers.put(chatRoomId, sessions.size());
            }
        }

        System.out.println("Connection closed. ChatRoomId: " + chatRoomId + ", Reason: " + status.getReason());
    }

    private String extractChatRoomId(String path) {
        if (path.startsWith("/chat/")) {
            return path.substring("/chat/".length()); // Extract everything after "/chat/"
        } else if (path.startsWith("/group-chat/group1")) {
            return path.substring("/group-chat/".length());
        }
        return null;
    }

    private void handleGroupAnnouncement(ChatMessage incomingMessage) throws IOException {
        List<WebSocketSession> sessions = chatRoomSessions.getOrDefault("group1", new ArrayList<>());
        for (WebSocketSession w : sessions) {
            if (w.isOpen()) {
                chatService.sendMessage(w, incomingMessage);
            }
        }
        System.out.println("Announcement broadcasted in group1: " + incomingMessage.getContent());
    }

    private void handlePrivateChat(String chatRoomId, ChatMessage incomingMessage, WebSocketSession session) throws IOException {
        switch (incomingMessage.getType()) {
            case "chat":
                handleChatMessage(chatRoomId, incomingMessage, session);
                break;
            case "typing":
                handleTypingNotification(incomingMessage, true);
                break;
            case "stop_typing":
                handleTypingNotification(incomingMessage, false);
                break;
            default:
                System.out.println("Unknown message type received: " + incomingMessage.getType());
        }
    }

    private void handleUnreadMessages(String chatRoomId) throws IOException {
        List<WebSocketSession> webSocketSessions = chatRoomSessions.get(chatRoomId);
        for (WebSocketSession w : webSocketSessions) {
            chatService.sendUnreadMessages(w, userUnreadMessages.getOrDefault(chatRoomId, new ArrayList<>()));
        }
    }

    private void handleChatMessage(String chatRoomId, ChatMessage incomingMessage, WebSocketSession session) throws IOException {
        if (chatRoomUsers.get(chatRoomId) < 2) {
            oneUserNotActiveMethod(incomingMessage);
        } else {
            allUsersActiveMethod(incomingMessage, chatRoomSessions.get(chatRoomId));
        }
    }

    private void oneUserNotActiveMethod(ChatMessage incomingMessage) {
        List<ChatMessage> chatMessages = userUnreadMessages.getOrDefault(incomingMessage.getChatRoomId(), new ArrayList<>());
        chatMessages.add(incomingMessage);
        userUnreadMessages.put(incomingMessage.getChatRoomId(), chatMessages);
    }

    private void allUsersActiveMethod(ChatMessage incomingMessage, List<WebSocketSession> sessions) throws IOException {
        incomingMessage.setSeen(true);
        chatService.sendMessageToSessions(incomingMessage, sessions);
    }

    private void handleTypingNotification(ChatMessage incomingMessage, boolean typing) throws IOException {
        chatService.checkIfTyping(incomingMessage, typing);
        List<WebSocketSession> sessions = chatRoomSessions.get(incomingMessage.getChatRoomId());
        chatService.sendMessageToSessions(incomingMessage, sessions);
    }
}
