package com.torchbell.lovecoach.gallery.model;

import com.torchbell.lovecoach.common.model.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class UserGallery extends BaseEntity {
    private Long userGalleryId;
    private Long userId;
    private Long galleryId;
    private Boolean isOpened;
    private Boolean isFavorite;
}
