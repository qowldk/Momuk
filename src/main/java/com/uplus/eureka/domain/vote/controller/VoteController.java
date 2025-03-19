package com.uplus.eureka.domain.vote.controller;

import com.uplus.eureka.config.JwtUtil;
import com.uplus.eureka.domain.vote.dto.VoteRequest;
import com.uplus.eureka.domain.vote.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/votes")
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
    @PostMapping("/create")
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

    
}
