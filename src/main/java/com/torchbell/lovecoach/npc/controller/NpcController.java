package com.torchbell.lovecoach.npc.controller;

import com.torchbell.lovecoach.common.constant.WebSessionKey;
import com.torchbell.lovecoach.npc.dto.request.ChatLogRequest;
import com.torchbell.lovecoach.npc.dto.request.ChatTalkRequest;
import com.torchbell.lovecoach.npc.dto.request.ReportRequest;
import com.torchbell.lovecoach.npc.dto.response.ChatLogResponse;
import com.torchbell.lovecoach.npc.dto.response.ChatTalkResponse;
import com.torchbell.lovecoach.npc.dto.response.NpcInfoResponse;
import com.torchbell.lovecoach.npc.service.NpcService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/npc")
@RequiredArgsConstructor
@Tag(name = "NpcController")
public class NpcController {

    private final NpcService npcService;
    private final String USER_ID_KEY = WebSessionKey.LOGIN_USER_ID.getKey();

    // npc 목록 조회
    @GetMapping
    public ResponseEntity<List<NpcInfoResponse>> getNpcList(HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        return ResponseEntity.ok(npcService.getNpcInfoList(userId));
    }


    // 대화 로그 조회
    @GetMapping("/chat")
    public ResponseEntity<List<ChatLogResponse>>  getChatLog(
            @ModelAttribute ChatLogRequest request,
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        return ResponseEntity.ok(npcService.getChatLogList(userId,request));
    }

    // 대화하기
    @PostMapping("/chat")
    public ResponseEntity<ChatTalkResponse> chat(
            @RequestBody ChatTalkRequest request,
            HttpSession session
    ){
        Long userId = (Long) session.getAttribute(USER_ID_KEY);

        return ResponseEntity.ok(npcService.getChatTalk(userId,request));
    }

    // AI 분석 리포트 생성
    @PostMapping("/report")
    public ResponseEntity<String> createReport(
            @RequestBody ReportRequest request,
            HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        // String response = npcService.createReport(userId, request);
        return ResponseEntity.ok(npcService.createReport(userId, request));
    }


}
