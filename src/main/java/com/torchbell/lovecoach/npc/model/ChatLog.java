package com.torchbell.lovecoach.npc.model;

import com.torchbell.lovecoach.common.model.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatLog extends BaseEntity {
    private Long chatId;
    private Long userId;
    private Long npcId;
    private String messageUser;
    private String messageAi;
    private String context = "없음";

}
