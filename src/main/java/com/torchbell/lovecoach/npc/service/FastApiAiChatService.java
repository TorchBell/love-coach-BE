package com.torchbell.lovecoach.npc.service;

import com.torchbell.lovecoach.npc.model.ChatLog;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@Primary
@RequiredArgsConstructor
public class FastApiAiChatService implements AiChatService {

    private final RestClient.Builder restClientBuilder;
    private static final String FASTAPI_URL = "http://localhost:8000"; // Python server URL

    @Override
    public String getChat(String systemPrompt, String context, List<ChatLog> chatLogList, String newChat) {
        RestClient restClient = restClientBuilder.build();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("system_prompt", systemPrompt);
        requestBody.put("context", context);
        // ChatLog list needs to be converted to a suitable format for Python
        requestBody.put("chat_log", chatLogList);
        requestBody.put("new_chat", newChat);

        try {
            Map<String, Object> response = restClient.post()
                    .uri(FASTAPI_URL + "/chat")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            if (response != null && response.containsKey("response")) {
                return (String) response.get("response");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "FastAPI 서버 연결 실패 또는 응답 오류";
    }

    @Override
    public String getContext(String context, List<ChatLog> chatLogList, String userMessage, String aiMessage) {
        RestClient restClient = restClientBuilder.build();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("context", context);
        requestBody.put("chat_log", chatLogList);
        requestBody.put("user_message", userMessage);
        requestBody.put("ai_message", aiMessage);

        try {
            Map<String, Object> response = restClient.post()
                    .uri(FASTAPI_URL + "/context")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            if (response != null && response.containsKey("new_context")) {
                return (String) response.get("new_context");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context; // Fallback to existing context
    }

    @Override
    public String getReport(String reportType, List<?> logs, Map<String, Object> statistics) {
        RestClient restClient = restClientBuilder.build();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("report_type", reportType);
        requestBody.put("logs", logs);
        requestBody.put("statistics", statistics);

        try {
            Map<String, Object> response = restClient.post()
                    .uri(FASTAPI_URL + "/report")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            if (response != null && response.containsKey("report")) {
                return (String) response.get("report");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "리포트 생성 실패";
    }


}
