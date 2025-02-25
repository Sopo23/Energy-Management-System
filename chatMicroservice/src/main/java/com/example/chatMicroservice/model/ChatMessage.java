package com.example.chatMicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    private String chatRoomId;      // Identifier for the chat room
    private String type;       // Type of message (CHAT, TYPING, STOP_TYPING)
    private Sender sender;          // Details of the message sender
    private String content;         // The actual message content
    private boolean seen;
    // Whether the message has been read
}
