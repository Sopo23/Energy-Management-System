package com.example.chatMicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    private String chatRoomId;
    private String type;
    private Sender sender;
    private String content;
    private boolean seen;
}
