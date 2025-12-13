package com.torchbell.lovecoach.gallery.dao;

import com.torchbell.lovecoach.gallery.dto.response.GalleryResponse;
import com.torchbell.lovecoach.gallery.model.UserGallery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface GalleryDao {
    // 갤러리 목록 조회 (유저 상태 포함)
    List<GalleryResponse> selectGalleryList(@Param("userId") Long userId);

    // 유저 갤러리 관계 조회
    Optional<UserGallery> selectUserGallery(@Param("userId") Long userId, @Param("galleryId") Long galleryId);

    // 유저 갤러리 관계 생성 (해금)
    int insertUserGallery(UserGallery userGallery);

    // 유저 갤러리 상태 수정
    int updateUserGallery(UserGallery userGallery);
}
