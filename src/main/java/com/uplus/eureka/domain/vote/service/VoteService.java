package com.uplus.eureka.domain.vote.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.uplus.eureka.domain.vote.dto.VoteRequest;
import com.uplus.eureka.domain.vote.repository.VoteMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;  // 로그를 위한 추가

@Slf4j
@Service
@RequiredArgsConstructor
public class VoteService {
	private final VoteMapper voteMapper;
	
//	 @Transactional
	    public void createVote(VoteRequest voteRequest) {
		    log.info("메서드 호출됨");

		  if (voteRequest.getTitle() == null || voteRequest.getTitle().isEmpty()) {
	            throw new IllegalArgumentException("투표 제목은 필수입니다.");
	        }

	        if (voteRequest.getDeadline().isBefore(LocalDateTime.now())) {
	            throw new IllegalArgumentException("마감일은 현재 시간 이후여야 합니다.");
	        }

	        log.info("insertVote 호출 전에 투표 제목: {}", voteRequest.getTitle());
	        log.info("메서드 호출 전");
	        
	        // 데이터베이스에 투표 삽입
	        voteMapper.insertVote(voteRequest);

	        log.info("insertVote 실행 완료, 투표 제목: {}", voteRequest.getTitle());
	        
	    }
	}