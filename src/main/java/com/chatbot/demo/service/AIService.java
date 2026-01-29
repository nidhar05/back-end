package com.chatbot.demo.service;

import com.chatbot.demo.entity.ChatMessage;
import com.chatbot.demo.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class AIService {

    @Value("${ollama.api.url}")
    private String ollamaUrl;

    @Value("${ollama.model}")
    private String model;

    private final ChatRepository chatRepository;

    public AIService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public String processMessage(String userMessage) {

        try {
            // 1️⃣ Build Prakruti-focused prompt
            String prompt = """
You are Prakruti AI, an Ayurvedic health assistant.

Rules:
1. The user may answer in full sentences or paragraphs.
2. Ask ONLY ONE follow-up health question at a time.
3. Focus on symptoms, duration, sensation, and triggers.
4. Do NOT give Prakruti or disease assessment without symptoms.
5. Keep responses short, clear, and professional.

Task:
- Understand the user's symptoms.
- Ask the next relevant question for Prakruti assessment.

User input:
""" + userMessage;

            // 2️⃣ Proper JSON body (escaped)
            String body = "{"
                    + "\"model\":\"" + model + "\","
                    + "\"prompt\":\"" + prompt.replace("\"", "\\\"").replace("\n", "\\n") + "\","
                    + "\"stream\":false"
                    + "}";

            // 3️⃣ Call Ollama
            URL url = new URL(ollamaUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8)
            );

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            String res = response.toString();

            // 4️⃣ Extract AI reply
            String aiReply;
            if (res.contains("\"response\"")) {
                aiReply = res.split("\"response\":\"")[1].split("\"")[0];
            } else {
                aiReply = "Please describe your symptoms in more detail.";
            }

            // 5️⃣ Apply Prakruti logic ONLY if symptoms exist
            String finalReply;
            if (PrakrutiAnalyzer.hasSymptoms(userMessage)) {
                String prakruti = PrakrutiAnalyzer.assess(userMessage);
                finalReply = aiReply + "\n\n🧘 Prakruti Assessment (Preliminary): " + prakruti;
            } else {
                finalReply = aiReply;
            }

            // 6️⃣ Store chat in DB
            chatRepository.save(new ChatMessage(userMessage, finalReply));

            return finalReply;

        } catch (Exception e) {
            e.printStackTrace();
            return "⚠️ Unable to connect to Prakruti AI. Please ensure Ollama is running.";
        }
    }
}
