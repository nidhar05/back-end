package com.chatbot.demo.service;

import com.chatbot.demo.entity.ChatMessage;
import com.chatbot.demo.repository.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemoryService {

    private final ChatRepository chatRepository;

    public MemoryService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public String buildMemorySummary(String sessionId) {

        List<ChatMessage> history =
                chatRepository.findBySessionIdOrderByTimestampAsc(sessionId);

        if (history.isEmpty()) return "No prior patient information.";

        return history.stream()
                .filter(m -> m.getRole().equals("USER"))
                .map(ChatMessage::getMessage)
                .collect(Collectors.joining("; "));
    }
}
