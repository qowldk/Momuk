package com.uplus.eureka.domain.participant.controller;

import com.uplus.eureka.config.JwtUtil;
import com.uplus.eureka.domain.participant.dto.ParticipationRequest;  
import com.uplus.eureka.domain.participant.service.ParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@Slf4j
//@RestController
//@RequestMapping("/api/votes")
//@RequiredArgsConstructor
//@Tag(name = "투표 참여 API", description = "사용자가 특정 투표에 참여할 수 있는 API")
//public class ParticipantController {
//
//    private final ParticipantService participantService;
//
//    @Operation(summary = "투표 참여 등록", description = "특정 투표글에 사용자가 투표합니다.")
//    @PostMapping("/{voteId}/participants")
//    public ResponseEntity<String> participate(
//            @Parameter(description = "참여할 투표 ID", example = "1") @PathVariable Integer voteId,
//            @RequestBody ParticipationRequest request
//        ) {
//        log.info("투표 ID: {}, 사용자 ID: {}", voteId, request.getUserId());
//
//        // 투표 참여 처리
//        participantService.registerParticipation(voteId, request.getUserId());
//
//        log.info("투표 참여 완료: 투표 ID: {}, 사용자 ID: {}", voteId, request.getUserId());
//
//        return ResponseEntity.status(201).body("투표 참여가 완료되었습니다.");
//    }
//
//}

//@Slf4j
//@RestController
//@RequestMapping("/api/votes")
//@RequiredArgsConstructor
//@Tag(name = "투표 참여 API", description = "사용자가 특정 투표에 참여할 수 있는 API")
//public class ParticipantController {
//
//    private final ParticipantService participantService;
//    private final JwtUtil jwtUtil;
//
//    @Operation(summary = "투표 참여 등록", description = "특정 투표글에 사용자가 투표합니다.")
//    @PostMapping("/{voteId}/participants")
//    public ResponseEntity<String> participate(
//            @Parameter(description = "참여할 투표 ID", example = "1") @PathVariable Integer voteId,
//            @RequestHeader("Authorization") String token // JWT 토큰을 요청 헤더에서 가져옴
//        ) {
//        // token은 실제 "Bearer " 제거 후 실제 JWT 값만 추출한거임
//        String jwt = token;
//        String userId = jwtUtil.getUserIdFromToken(jwt); // JWT에서 userId 추출
//
//        log.info("투표 ID: {}, 사용자 ID: {}", voteId, userId);
//
//        // 투표 참여 처리
//        participantService.registerParticipation(voteId, userId);
//
//        log.info("투표 참여 완료: 투표 ID: {}, 사용자 ID: {}", voteId, userId);
//
//        return ResponseEntity.status(201).body("투표 참여가 완료되었습니다.");
//    }
//}
//
@Slf4j
@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
@Tag(name = "투표 참여 API", description = "사용자가 특정 투표에 참여할 수 있는 API")
public class ParticipantController {

    private final ParticipantService participantService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "투표 참여 등록", description = "특정 투표글에 사용자가 투표합니다.")
    @PostMapping("/{voteId}/participants")
    public ResponseEntity<String> participate(
            @Parameter(description = "참여할 투표 ID", example = "1") @PathVariable Integer voteId,
            @RequestHeader("Authorization") String token
        ) {
        // Bearer prefix 제거
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        // 토큰 유효성 검증
        if (!jwtUtil.checkToken(token)) {
            log.error("유효하지 않은 토큰: {}", token);
            return ResponseEntity.status(401).body("인증에 실패했습니다.");
        }
        
        // 유효한 토큰에서 userId 추출
        String userId = jwtUtil.getUserIdFromToken(token);
        log.info("투표 ID: {}, 사용자 ID: {}", voteId, userId);

        // 투표 참여 처리
        participantService.registerParticipation(voteId, userId);

        log.info("투표 참여 완료: 투표 ID: {}, 사용자 ID: {}", voteId, userId);

        return ResponseEntity.status(201).body("투표 참여가 완료되었습니다.");
    }
}

