package com.example.chatMicroservice.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ActiveUserService {
    private final ConcurrentHashMap<String, String> activeSessions = new ConcurrentHashMap<>();

    public void addUser(String sessionId, String username) {
        activeSessions.put(sessionId, username);
    }

    public void removeUser(String sessionId) {
        activeSessions.remove(sessionId);
    }

    public ConcurrentHashMap<String, String> getActiveUsers() {
        return activeSessions;
    }
}
