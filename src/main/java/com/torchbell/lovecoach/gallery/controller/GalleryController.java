package com.torchbell.lovecoach.gallery.controller;

import com.torchbell.lovecoach.common.constant.WebSessionKey;
import com.torchbell.lovecoach.gallery.dto.response.GalleryResponse;
import com.torchbell.lovecoach.gallery.service.GalleryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gallery")
@RequiredArgsConstructor
@Tag(name = "GalleryController")
public class GalleryController {

    private final GalleryService galleryService;
    private final String USER_ID_KEY = WebSessionKey.LOGIN_USER_ID.getKey();

    // 갤러리 목록 조회
    @GetMapping
    public ResponseEntity<List<GalleryResponse>> getGalleryList(HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        return ResponseEntity.ok(galleryService.getGalleryList(userId));
    }

    // 갤러리 해금
    @PatchMapping("/{galleryId}/unlock")
    public ResponseEntity<Map<String, Object>> unlockGallery(
            @PathVariable Long galleryId,
            HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        return ResponseEntity.ok(galleryService.unlockGallery(userId, galleryId));
    }
}
