package com.example.chatMicroservice.configuration;

import com.example.chatMicroservice.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatHandler(new ChatService(new ObjectMapper())), "/chat/{userId}")
                .setAllowedOrigins("*");

        registry.addHandler(new ChatHandler(new ChatService(new ObjectMapper())), "/group-chat/group1")
                .setAllowedOrigins("*");
    }
}
