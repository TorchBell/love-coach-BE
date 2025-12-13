package com.torchbell.lovecoach.npc.model;

import com.torchbell.lovecoach.common.model.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserNpc extends BaseEntity {
    private Long userNpcId;     // PK
    private Long userId;        // FK (User)
    private Long npcId;         // FK (Npc)
    private Integer affectionScore; // 호감도 (0~100)
    private String currentState;    // 상태 (NORMAL, HAPPY, ANGRY 등)
}