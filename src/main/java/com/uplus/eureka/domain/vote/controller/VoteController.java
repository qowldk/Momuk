package com.uplus.eureka.domain.vote.controller;

import com.uplus.eureka.config.JwtUtil;
import com.uplus.eureka.domain.vote.Vote;
import com.uplus.eureka.domain.vote.dto.VoteRequest;
import com.uplus.eureka.domain.vote.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/votes")
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@RequiredArgsConstructor
@Tag(name = "투표 생성 API", description = "사용자가 새로운 투표를 생성할 수 있는 API")
@SecurityRequirement(name = "BearerAuth") // Swagger에서 인증 필요하도록 설정


public class VoteController {
    private final VoteService voteService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "투표 생성",
    		description = "새로운 투표를 생성합니다.",
            security = @SecurityRequirement(name = "bearerAuth")  // 개별 메서드에서도 인증 필요 명시 가능

    		)
    @PostMapping("")
    public ResponseEntity<String> createVote(
    		@RequestHeader(value = "Authorization") String token,
            @RequestBody VoteRequest voteRequest
    ) {
    	
        log.info("Authorization Header: {}", token); // 토큰 출력

    	 if (token == null || token.isEmpty()) {
    	        log.error("Authorization 헤더가 누락되었습니다.");
    	        return ResponseEntity.status(401).body("인증 정보가 없습니다.");
    	    }
    	
    	 // Bearer가 여러 번 붙었을 경우 하나만 제거
         if (token.startsWith("Bearer ")) {
             token = token.replaceFirst("Bearer ", "").trim();
         }

        // 토큰 유효성 검사
        if (!jwtUtil.checkToken(token)) {
            log.error("유효하지 않은 토큰: {}", token);
            return ResponseEntity.status(401).body("인증에 실패했습니다.");
        }

        // JWT에서 userId 추출 (creatorId로 사용)
        String creatorId = jwtUtil.getUserIdFromToken(token);
        log.info("투표 생성 요청: 사용자 ID: {}, 투표 데이터: {}", creatorId, voteRequest);

        // 투표 생성
        voteRequest.setCreatorId(creatorId);

        voteService.createVote(voteRequest);

        log.info("투표 생성 완료: {}", voteRequest);
        return ResponseEntity.status(201).body("투표가 성공적으로 생성되었습니다.");
    }


    /* 투표 수정
     *
     * */
    @Operation(summary = "투표 수정", description = "기존 투표를 수정합니다.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{voteId}")
    public ResponseEntity<String> updateVote(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer voteId,
            @RequestBody VoteRequest voteRequest
    ) {
        log.info("Authorization Header: {}", token);

        // Bearer 토큰 처리
        if (token.startsWith("Bearer ")) {
            token = token.replaceFirst("Bearer ", "").trim();
        }

        // 토큰 유효성 검사
        if (!jwtUtil.checkToken(token)) {
            log.error("유효하지 않은 토큰: {}", token);
            return ResponseEntity.status(401).body("인증에 실패했습니다.");
        }

        // JWT에서 userId 추출 (작성자인지 확인)
        String userId = jwtUtil.getUserIdFromToken(token);
        log.info("투표 수정 요청: 사용자 ID: {}, 투표 ID: {}, 수정 데이터: {}", userId, voteId, voteRequest);

        // 투표 수정 서비스 호출
        voteService.updateVote(voteId, voteRequest, userId);

        log.info("투표 수정 완료: {}", voteRequest);
        return ResponseEntity.ok("투표가 성공적으로 수정되었습니다.");
    }

    /**
     * JWT 인증을 이용한 투표 삭제 API
     */
    @DeleteMapping("/{voteId}")
    @Operation(summary = "투표 삭제", description = "작성자가 자신의 투표를 삭제할 수 있습니다.")
    public ResponseEntity<String> deleteVote(
            @PathVariable int voteId,
            @RequestHeader("Authorization") String token
    ) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            if (!jwtUtil.checkToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
            }

            String userId = jwtUtil.getUserIdFromToken(token);
            boolean success = voteService.deleteVote(voteId, userId);

            if (success) {
                return ResponseEntity.status(HttpStatus.OK).body("투표가 삭제되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
            }

        } catch (Exception e) {
            log.error("투표 삭제 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
        }
    }

    /**
     * ✅ 투표 마감 API (JWT 인증 필요)
     */
    @PatchMapping("/close/{voteId}")
    @Operation(summary = "투표 마감", description = "투표 생성자가 직접 투표를 마감할 수 있습니다.")
    public ResponseEntity<String> closeVote(@PathVariable Integer voteId, @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        boolean success = voteService.closeVote(voteId, token);
        return success ? ResponseEntity.ok("투표가 마감되었습니다.") :
                ResponseEntity.badRequest().body("이미 마감된 투표이거나 존재하지 않는 투표입니다.");
    }

    /**
     * ✅ 자동 마감 트리거 API
     */
    @PatchMapping("/auto-close")
    @Operation(summary = "자동 마감", description = "마감 시간이 지난 투표를 자동 마감합니다.")
    public ResponseEntity<String> triggerAutoCloseVotes() {
        boolean success = voteService.autoCloseVotes();
        return success ? ResponseEntity.ok("기한이 지난 투표가 자동 마감되었습니다.") :
                ResponseEntity.ok("자동 마감할 투표가 없습니다.");
    }

    /**
     * ✅ 특정 투표글 상태 조회 API
     */
    @GetMapping("/{voteId}/status")
    @Operation(summary = "투표 상태 조회", description = "특정 투표글의 상태(active/closed)를 조회합니다.")
    public ResponseEntity<?> getVoteStatus(@PathVariable Integer voteId) {
        Vote vote = voteService.getVoteStatus(voteId);

        if (vote == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 투표글을 찾을 수 없습니다.");
        }

        return ResponseEntity.ok(vote);
    }

}
