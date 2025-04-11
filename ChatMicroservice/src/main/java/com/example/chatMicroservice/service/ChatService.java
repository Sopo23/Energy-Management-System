package com.example.chatMicroservice.service;

import com.example.chatMicroservice.model.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatService {

    private final ObjectMapper objectMapper;


    /**
     * Sends a message to a single WebSocket session.
     *
     * @param session The WebSocket session to send the message to.
     * @param message The ChatMessage object to send.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    public void sendMessage(WebSocketSession session, ChatMessage message) throws IOException {
        String messageJson = objectMapper.writeValueAsString(message);
        if (session.isOpen()) {
            session.sendMessage(new TextMessage(messageJson));
        }
    }

    /**
     * Sends a message to all sessions in a list.
     *
     * @param message  The ChatMessage object to send.
     * @param sessions The list of WebSocket sessions to send the message to.
     * @throws IOException If an I/O error occurs while sending the messages.
     */
    public void sendMessageToSessions(ChatMessage message, List<WebSocketSession> sessions) throws IOException {
        String messageJson = objectMapper.writeValueAsString(message);
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(messageJson));
            }
        }
    }

    /**
     * Sends unread messages to a specific WebSocket session.
     *
     * @param session        The WebSocket session to send the messages to.
     * @param unreadMessages The list of unread ChatMessage objects.
     * @throws IOException If an I/O error occurs while sending the messages.
     */
    public void sendUnreadMessages(WebSocketSession session, List<ChatMessage> unreadMessages) throws IOException {
        for (ChatMessage message : unreadMessages) {
            sendMessage(session, message);
        }
    }

    /**
     * Checks if a user is typing and logs the typing status.
     *
     * @param message The ChatMessage object containing typing information.
     * @param typing  True if the user is typing, false otherwise.
     */
    public void checkIfTyping(ChatMessage message, boolean typing) {
        System.out.println("User " + message.getSender().getSenderUsername() +
                (typing ? " is typing..." : " stopped typing."));
    }
}
