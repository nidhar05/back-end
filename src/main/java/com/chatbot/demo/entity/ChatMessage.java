package com.chatbot.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String userMessage;

    @Column(columnDefinition = "TEXT")
    private String aiReply;

    private LocalDateTime timestamp;

    public ChatMessage() {}

    public ChatMessage(String userMessage, String aiReply) {
        this.userMessage = userMessage;
        this.aiReply = aiReply;
        this.timestamp = LocalDateTime.now();
    }

    // getters & setters
}
