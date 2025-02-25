package com.example.chatMicroservice.controller;

import com.example.chatMicroservice.model.ChatMessage;
import com.example.chatMicroservice.model.MessageType;
import com.example.chatMicroservice.model.Sender;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat/{userId}")
public class ChatController {

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message, @PathVariable String userId) {
        // Attach the sender information and chat room ID dynamically
        message.setSender(new Sender(userId, "Unknown")); // Replace "Unknown" with an actual username lookup
        message.setChatRoomId("chat-room-" + userId); // Create chat room ID dynamically
        message.setSeen(false); // Mark as unseen initially
        return message;
    }

    @MessageMapping("/typing")
    @SendTo("/topic/typing")
    public ChatMessage notifyTyping(ChatMessage message, @PathVariable String userId) {
        // Create a typing notification message
        message.setType("typing");
        message.setSender(new Sender(userId, "Unknown")); // Replace "Unknown" with an actual username lookup
        message.setChatRoomId("chat-room-" + userId); // Add chat room ID dynamically
        return message;
    }
}
