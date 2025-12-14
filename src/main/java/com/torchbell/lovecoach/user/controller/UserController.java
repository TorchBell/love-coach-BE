package com.torchbell.lovecoach.user.controller;

import com.torchbell.lovecoach.common.constant.WebSessionKey;
import com.torchbell.lovecoach.user.dto.request.CreditUsageRequest;
import com.torchbell.lovecoach.user.dto.request.UserJoinRequest;
import com.torchbell.lovecoach.user.dto.request.UserLoginRequest;
import com.torchbell.lovecoach.user.dto.request.UserUpdateRequest;
import com.torchbell.lovecoach.user.dto.response.CreditUsageResponse;
import com.torchbell.lovecoach.user.dto.response.UserInfoResponse;
import com.torchbell.lovecoach.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "UserController")
public class UserController {

    private final UserService userService;
    private final String USER_ID_KEY = WebSessionKey.LOGIN_USER_ID.getKey();

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }

    // 유저 정보 조회
    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> info(HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        UserInfoResponse info = userService.info(userId);
        return ResponseEntity.ok(info);
    }



    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody UserJoinRequest request) {
        userService.join(request);
        return ResponseEntity.ok().build();
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserInfoResponse> login(
            @RequestBody UserLoginRequest request,
            HttpSession session
    ) {
        UserInfoResponse userInfo = userService.login(request);
        session.setAttribute(USER_ID_KEY, userInfo.getUserId());
        return ResponseEntity.ok().body(userInfo);
    }

    // 크레딧 사용
    @PostMapping("/credit-usage")
    public ResponseEntity<CreditUsageResponse>  creditUsage(
            @RequestBody CreditUsageRequest request,
            HttpSession session
    ){
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        return ResponseEntity.ok(userService.creditUsage(request,userId));
    }

    // 내 정보 수정
    @PatchMapping
    public ResponseEntity<UserInfoResponse> updateUser(
            @RequestBody UserUpdateRequest request,
            HttpSession session
    ){
        Long userId =  (Long) session.getAttribute(USER_ID_KEY);
        return ResponseEntity.ok(userService.updateUser(request,userId));
    }

    // 회원 탈퇴 (soft delete)
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(
            HttpSession session
    ){
        Long userId =  (Long) session.getAttribute(USER_ID_KEY);
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }


}
