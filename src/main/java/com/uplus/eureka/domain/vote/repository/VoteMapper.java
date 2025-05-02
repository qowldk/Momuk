package com.uplus.eureka.domain.vote.repository;

import java.util.Map;
import java.util.List;

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

    void incrementParticipants(Integer voteId);
    
    //투표 생성
    void insertVote(VoteRequest voteRequest);
    Integer getLastInsertedVoteId();
    void insertCreatorAsParticipant(Map<String, Object> paramMap);

    
    // 투표글 ID로 작성자 ID 조회
    String getVoteCreator(@Param("voteId") Integer voteId);
    
    

    /**
     * 특정 투표글에 참여한 사용자 데이터 삭제
     */
    void deleteParticipants(@Param("voteId") Integer voteId);

    /**
     * 작성자가 맞을 경우 투표 삭제
     */
    void deleteVote(@Param("voteId") Integer voteId);

    /**
     * 특정 투표글 마감 (status 변경)
     */
    int closeVote(@Param("voteId") Integer voteId);

    // 투표 생성자(최초 참여자) 조회
    String getVoteOwner(@Param("voteId") Integer voteId);

    /**
     * ✅ 마감된 투표 자동 업데이트 (현재 시간과 비교)
     */
    int autoCloseVotes();

    /**
     * 특정 투표글 상태 조회
     */
    Vote getVoteStatus(@Param("voteId") Integer voteId);
  
    void decrementParticipant(Integer voteId);

    //투표 수정
    void updateVote(@Param("voteId") Integer voteId, @Param("voteRequest") VoteRequest voteRequest);

    // 전체 투표글 조회
    List<Vote> getAllVotes();

}
