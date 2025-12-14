package com.torchbell.lovecoach.npc.service;

import com.torchbell.lovecoach.npc.model.ChatLog;
import java.util.List;

public interface AiChatService {
    String getChat(String systemPrompt, String context, List<ChatLog> chatLogList, String newChat);

    String getContext(String context, List<ChatLog> chatLogList, String userMessage, String aiMessage);
}
