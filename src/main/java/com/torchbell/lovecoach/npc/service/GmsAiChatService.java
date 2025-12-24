package com.torchbell.lovecoach.npc.service;

import com.torchbell.lovecoach.npc.dto.gms.GmsChatRequest;
import com.torchbell.lovecoach.npc.dto.gms.GmsChatResponse;
import com.torchbell.lovecoach.npc.model.ChatLog;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

//@Service
@RequiredArgsConstructor
public class GmsAiChatService implements AiChatService {

    @Value("${gms.url}")
    private String gmsUrl;

    @Value("${gms.api-key}")
    private String gmsApiKey;

    @Value("${gms.model}")
    private String gmsModel;

    private final RestClient.Builder restClientBuilder;

    // 채팅 답변해주는 메서드
    public String getChat(String systemPrompt, String context, List<ChatLog> chatLogList, String newChat) {
        String finalSystemPrompt = systemPrompt + "\n[Context Info]: " + context;
        System.out.println(finalSystemPrompt);
        // 2. 메시지 구성
        List<GmsChatRequest.Message> messages = new ArrayList<>();
        messages.add(GmsChatRequest.Message.builder().role("system").content(finalSystemPrompt).build());

        List<ChatLog> reversedLogs = new ArrayList<>(chatLogList);
        Collections.reverse(reversedLogs);
        for (ChatLog log : reversedLogs) {
            if (log.getMessageUser() != null && log.getMessageAi() != null) {
                messages.add(GmsChatRequest.Message.builder().role("user").content(log.getMessageUser()).build());
                messages.add(GmsChatRequest.Message.builder().role("assistant").content(log.getMessageAi()).build());
            }
        }
        messages.add(GmsChatRequest.Message.builder().role("user").content(newChat).build());
        return callGmsApi(messages);
    }

    // 문맥 만들어주는 메서드
    public String getContext(String context, List<ChatLog> chatLogList, String userMessage,
                             String aiMessage) {
        String summaryInstruction = "당신은 대화의 핵심 맥락을 요약하고 관리하는 AI입니다. \n" +
                "주어진 [기존 문맥]과 [최근 대화 내역]을 종합하여, 다음 대화에서 캐릭터가 참고할 수 있는 '새로운 문맥'을 작성해주세요.\n" +
                "사용자의 취향, 중요한 사건, 대화의 흐름 등을 중심으로 간결하게 요약하세요.";
        StringBuilder recentChat = new StringBuilder();

        // 최근 대화 5개 정도만 포함 (너무 길면 토큰 낭비)
        // chatLogList는 최신순(DESC)이므로 앞쪽이 최근 대화임.
        // 최근 5개를 가져와서 시간순(ASC)으로 정렬해야 함.
        int count = Math.min(5, chatLogList.size());
        List<ChatLog> recentLogs = new ArrayList<>(chatLogList.subList(0, count));
        Collections.reverse(recentLogs);

        for (ChatLog log : recentLogs) {
            recentChat.append("User: ").append(log.getMessageUser()).append("\n")
                    .append("AI: ").append(log.getMessageAi()).append("\n");
        }
        recentChat.append("User: ").append(userMessage).append("\n")
                .append("AI: ").append(aiMessage).append("\n");
        String userPrompt = String.format("[기존 문맥]: %s\n\n[최근 대화 내역]:\n%s",
                (context != null ? context : "없음"),
                recentChat.toString());

        List<GmsChatRequest.Message> messages = new ArrayList<>();
        messages.add(GmsChatRequest.Message.builder().role("system").content(summaryInstruction).build());
        messages.add(GmsChatRequest.Message.builder().role("user").content(userPrompt).build());

        return callGmsApi(messages);
    }

    @Override
    public String getReport(String reportType, List<?> logs, Map<String, Object> statistics) {
        return "GMS 서비스는 리포트 생성을 지원하지 않습니다.";
    }

    // API 호출
    private String callGmsApi(List<GmsChatRequest.Message> messages) {
        RestClient restClient = restClientBuilder.build();

        GmsChatRequest request = GmsChatRequest.builder()
                .model(gmsModel)
                .messages(messages)
                .build();

        try {
            // JSON 변환 및 로그 출력
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String jsonBody = mapper.writeValueAsString(request);
            System.out.println("--------request json--------");
            System.out.println(jsonBody);
            System.out.println("----------------------------");

            GmsChatResponse response = restClient.post()
                    .uri(gmsUrl)
                    .header("Authorization", "Bearer " + gmsApiKey)
                    .header("User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonBody) // JSON 문자열을 직접 전송
                    .retrieve()
                    .body(GmsChatResponse.class);

            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                return response.getChoices().get(0).getMessage().getContent();
            }
        } catch (Exception e) {
            System.out.println("API 호출 중 에러 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return "죄송해요, 지금은 대화하기가 조금 어려워요.";
    }
}
