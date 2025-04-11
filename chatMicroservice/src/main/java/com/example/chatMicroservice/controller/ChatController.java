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
        message.setSender(new Sender(userId, "Unknown"));
        message.setChatRoomId("chat-room-" + userId);
        message.setSeen(false);
        return message;
    }

    @MessageMapping("/typing")
    @SendTo("/topic/typing")
    public ChatMessage notifyTyping(ChatMessage message, @PathVariable String userId) {
        message.setType("typing");
        message.setSender(new Sender(userId, "Unknown"));
        message.setChatRoomId("chat-room-" + userId);
        return message;
    }
}
