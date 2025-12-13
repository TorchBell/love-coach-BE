package com.torchbell.lovecoach.gallery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GalleryResponse {
    private Long galleryId;
    private String title;
    private String imageUrl;
    private String unlockCondition;
    private Boolean isOpened;
    private Boolean isFavorite;
    // B-Cut and Letter might be shown only in detail view or if opened.
    // Spec says "Gallery List" has these fields.
}
