package com.uplus.eureka.domain.vote.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.uplus.eureka.domain.vote.Vote;
import com.uplus.eureka.domain.vote.dto.VoteRequest;

@Mapper
@Repository
public interface VoteMapper {

    // 투표글 ID로 투표 조회
    Vote getVoteById(Integer voteId);

    // 투표 참여자 수 증가
    void incrementParticipants(Integer voteId);
    
    //투표 생성
    void insertVote(VoteRequest voteRequest);
  
    void decrementParticipant(Integer voteId);

    //투표 수정
    void updateVote(@Param("voteId") Integer voteId, @Param("voteRequest") VoteRequest voteRequest);

}
