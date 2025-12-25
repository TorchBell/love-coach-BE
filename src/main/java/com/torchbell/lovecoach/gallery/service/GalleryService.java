package com.torchbell.lovecoach.gallery.service;

import com.torchbell.lovecoach.gallery.dao.GalleryDao;
import com.torchbell.lovecoach.gallery.dto.response.GalleryResponse;
import com.torchbell.lovecoach.gallery.model.UserGallery;
import com.torchbell.lovecoach.notification.service.NotificationService;
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
    private final NotificationService notificationService;
    private final com.torchbell.lovecoach.user.service.UserService userService;

    // 갤러리 목록 조회
    @Transactional(readOnly = true)
    public List<GalleryResponse> getGalleryList(Long userId) {
        return galleryDao.selectGalleryList(userId);
    }

    // B컷 해금 (구 갤러리 해금)
    @Transactional
    public Map<String, Object> unlockGallery(Long userId, Long galleryId) {
        // 크레딧 차감 (500)
        userService.updateCredit(com.torchbell.lovecoach.user.dto.request.CreditUsageRequest.builder()
                .amount(-500)
                .build(), userId);

        Optional<UserGallery> optionalUserGallery = galleryDao.selectUserGallery(userId, galleryId);

        UserGallery userGallery;
        if (optionalUserGallery.isPresent()) {
            userGallery = optionalUserGallery.get();
            if (Boolean.TRUE.equals(userGallery.getIsBOpened())) {
                // 이미 해금됨
                Map<String, Object> result = new HashMap<>();
                result.put("galleryId", galleryId);
                result.put("isBOpened", true);
                result.put("message", "이미 해금된 B컷입니다.");
                return result;
            }
            userGallery.setIsBOpened(true);
            galleryDao.updateUserGallery(userGallery);
        } else {
            // 존재하지 않으면 새로 생성 (isOpened=false, isBOpened=true)
            userGallery = UserGallery.builder()
                    .userId(userId)
                    .galleryId(galleryId)
                    .isOpened(false)
                    .isBOpened(true)
                    .build();
            galleryDao.insertUserGallery(userGallery);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("galleryId", galleryId);
        result.put("isBOpened", true);
        result.put("message", "B컷이 해금되었습니다.");

        // 알림 전송 (필요시 타입 변경 고려, 일단 gallery 유지)
        notificationService.send(userId, "gallery", result);

        return result;
    }
}
