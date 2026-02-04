package com.chatbot.demo.controller;

import com.chatbot.demo.dto.ChatRequest;
import com.chatbot.demo.dto.ChatResponse;
import com.chatbot.demo.service.AIService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    private final AIService aiService;

    public ChatController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        String reply = aiService.processMessage(
                request.getSessionId(),
                request.getMessage()
        );
        return new ChatResponse(reply);
    }
}
