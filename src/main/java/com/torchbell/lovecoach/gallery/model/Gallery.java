package com.torchbell.lovecoach.gallery.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Gallery {
    private Long galleryId;
    private String title;
    private String imageUrl;
    private String bCutImageUrl;
    private String letter;
    private String unlockCondition;
}
