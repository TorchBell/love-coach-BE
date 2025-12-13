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
public class Npc extends BaseEntity {
    private Long npcId;
    private String name;
    private String personality;
}
