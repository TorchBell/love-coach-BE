package com.torchbell.lovecoach.npc.service;

import com.torchbell.lovecoach.npc.model.ChatLog;
import java.util.List;
import java.util.Map;

public interface AiChatService {
    String getChat(String systemPrompt, String context, List<ChatLog> chatLogList, String newChat);

    String getContext(String context, List<ChatLog> chatLogList, String userMessage, String aiMessage);

    // 분석 리포트 생성
    String getReport(String reportType, List<?> logs, Map<String, Object> statistics);

}
