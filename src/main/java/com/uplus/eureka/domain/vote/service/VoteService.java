package com.uplus.eureka.domain.vote.service;

import java.time.LocalDateTime;

import com.uplus.eureka.config.JwtUtil;
import com.uplus.eureka.domain.vote.Vote;
import org.springframework.scheduling.annotation.Scheduled;
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
	private final JwtUtil jwtUtil;
	
//	 @Transactional
//	 @Transactional
public void createVote(VoteRequest voteRequest, String creatorId) {
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
	voteMapper.insertVote(voteRequest, creatorId);

	log.info("insertVote 실행 완료, 투표 제목: {}", voteRequest.getTitle());

}


	/**
	 * ✅ JWT 토큰을 이용하여 투표 삭제 (최초 참여자만 삭제 가능)
	 */
	@Transactional
	public boolean deleteVote(int voteId, String userId) {
		// 해당 투표의 작성자 ID 가져오기
		String creatorId = voteMapper.getVoteCreator(voteId);

		// 작성자 본인인지 확인
		if (creatorId == null || !creatorId.equals(userId)) {
			return false;  // 삭제 권한 없음
		}

		// 3. 투표에 참여한 사용자 삭제
		voteMapper.deleteParticipants(voteId);

		// 4. 투표 삭제
		voteMapper.deleteVote(voteId);
		return true;
	}

	/**
	 * ✅ 작성자가 직접 투표를 마감할 수 있는 기능
	 */
	@Transactional
	public boolean closeVote(Integer voteId, String token) {
		// JWT 토큰에서 userId 추출
		String userId = jwtUtil.getUserIdFromToken(token);

		// 투표 작성자 ID 가져오기
		String creatorId = voteMapper.getVoteCreator(voteId);

		// 작성자 본인인지 확인
		if (creatorId == null || !creatorId.equals(userId)) {
			throw new RuntimeException("투표 마감 권한이 없습니다.");
		}

		// 투표 마감 실행
		int result = voteMapper.closeVote(voteId);
		return result > 0; // 변경된 행이 1개 이상이면 true 반환
	}

	/**
	 * ✅ 투표 마감 시간이 지난 투표 자동 마감 (1분마다 실행)
	 */
	@Transactional
	@Scheduled(fixedRate = 60000) // 1분마다 실행
	public boolean autoCloseVotes() {
		int closedVotes = voteMapper.autoCloseVotes();
		System.out.println("자동 마감된 투표 수: " + closedVotes);
		return closedVotes > 0; // 마감된 투표가 있으면 true 반환
	}

	/**
	 * ✅ 특정 투표글 상태 조회
	 */
	@Transactional(readOnly = true)
	public Vote getVoteStatus(Integer voteId) {
		return voteMapper.getVoteStatus(voteId);
	}
}