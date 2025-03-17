package com.uplus.eureka.domain.vote.repository;

import org.springframework.stereotype.Repository;
import com.uplus.eureka.domain.vote.Vote;

@Repository
public interface VoteMapper {

    // 투표글 ID로 투표 조회
    Vote getVoteById(Integer voteId);

    // 투표 참여자 수 증가
    void incrementParticipants(Integer voteId);
}
