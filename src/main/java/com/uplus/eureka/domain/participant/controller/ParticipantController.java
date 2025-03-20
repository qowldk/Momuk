package com.uplus.eureka.domain.participant.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uplus.eureka.config.JwtUtil;
import com.uplus.eureka.domain.participant.Participant;
import com.uplus.eureka.domain.participant.service.ParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


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

    @Operation(summary = "투표 참여 취소", description = "특정 투표글에 대한 사용자의 투표 참여를 취소합니다.")
    @DeleteMapping("/{voteId}/participants")
    public ResponseEntity<String> cancelParticipation(
            @Parameter(description = "취소할 투표 ID", example = "1") @PathVariable Integer voteId,
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

        // 투표 참여 취소 처리
        try {
            participantService.cancelParticipation(voteId, userId);
        } catch (RuntimeException e) {
            log.error("투표 참여 취소 실패: 투표 ID: {}, 사용자 ID: {}, 이유: {}", voteId, userId, e.getMessage());
            return ResponseEntity.status(400).body("투표 참여 취소에 실패했습니다.");
        }

        log.info("투표 참여 취소 완료: 투표 ID: {}, 사용자 ID: {}", voteId, userId);

        return ResponseEntity.status(200).body("투표 참여가 취소되었습니다.");
    }
    @Operation(summary = "투표 참여자 리스트 조회", description = "특정 투표글에 참여한 사용자의 리스트를 조회합니다.")
    @GetMapping("/{voteId}/participantList")
    public ResponseEntity<?> getParticipants(
            @Parameter(description = "참여자 리스트를 조회할 투표 ID", example = "1") @PathVariable Integer voteId,
            @RequestHeader("Authorization") String token
    ) {
        // Bearer prefix 제거
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 토큰 유효성 검증
        if (!jwtUtil.checkToken(token)) {
            log.error("유효하지 않은 토큰: {}", token);
            return ResponseEntity.status(401).body("{\"error\": \"Unauthorized\"}");
        }

        // 유효한 토큰에서 userId 추출
        String userId = jwtUtil.getUserIdFromToken(token);
        log.info("투표 ID: {}, 사용자 ID: {}", voteId, userId);

        // 참여자 리스트 조회
        List<Participant> participants = participantService.getParticipants(voteId);
        log.info("투표 참여자 리스트 조회 완료: 투표 ID: {}, 참여자 수: {}", voteId, participants.size());
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\"participants\": [");

        for (int i = 0; i < participants.size(); i++) {
            Participant p = participants.get(i);
            if (p != null) {
                jsonBuilder.append(String.format("{\"id\": %d, \"name\": \"%s\"}", p.getParticipantId(), p.getUserId()));
                if (i < participants.size() - 1) {
                    jsonBuilder.append(",");
                }
            }
        }

        jsonBuilder.append("]}");
//        int i = 1;
//        for (Participant p : participants) {
//            log.info("투표 참여자 리스트: N.{} {}", i, p.getUserId());
//            i++;
//        }
        // JSON 응답 (바로 리스트 반환)
        return ResponseEntity.status(200).body(jsonBuilder.toString());
    }
}
