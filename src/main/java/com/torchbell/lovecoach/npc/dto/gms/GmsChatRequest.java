package com.torchbell.lovecoach.npc.dto.gms;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GmsChatRequest {
    private String model;
    private List<Message> messages;

    @Getter
    @Builder
    public static class Message {
        private String role;
        private String content;
    }
}
