package com.torchbell.lovecoach.gallery.service;

import com.torchbell.lovecoach.gallery.dao.GalleryDao;
import com.torchbell.lovecoach.gallery.dto.response.GalleryResponse;
import com.torchbell.lovecoach.gallery.model.UserGallery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GalleryService {
    private final GalleryDao galleryDao;

    // 갤러리 목록 조회
    @Transactional(readOnly = true)
    public List<GalleryResponse> getGalleryList(Long userId) {
        return galleryDao.selectGalleryList(userId);
    }

    // 갤러리 해금
    @Transactional
    public Map<String, Object> unlockGallery(Long userId, Long galleryId) {
        Optional<UserGallery> optionalUserGallery = galleryDao.selectUserGallery(userId, galleryId);

        UserGallery userGallery;
        if (optionalUserGallery.isPresent()) {
            userGallery = optionalUserGallery.get();
            if (userGallery.getIsOpened()) {
                // 이미 해금됨
                Map<String, Object> result = new HashMap<>();
                result.put("galleryId", galleryId);
                result.put("isOpened", true);
                result.put("message", "이미 해금된 갤러리입니다.");
                return result;
            }
            userGallery.setIsOpened(true);
            galleryDao.updateUserGallery(userGallery);
        } else {
            userGallery = UserGallery.builder()
                    .userId(userId)
                    .galleryId(galleryId)
                    .isOpened(true)
                    .isFavorite(false)
                    .build();
            galleryDao.insertUserGallery(userGallery);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("galleryId", galleryId);
        result.put("isOpened", true);
        result.put("message", "갤러리가 해금되었습니다.");
        return result;
    }
}
