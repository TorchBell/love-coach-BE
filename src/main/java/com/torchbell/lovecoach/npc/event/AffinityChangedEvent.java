package com.torchbell.lovecoach.npc.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AffinityChangedEvent {
    private Long userId;
    private Long npcId;
    private int newScore;
}
