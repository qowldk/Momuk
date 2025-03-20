package com.uplus.eureka.domain.participant.repository;


import org.apache.ibatis.annotations.Param;
import com.uplus.eureka.domain.participant.Participant;
import java.util.List;

public interface ParticipantMapper {
    void insertParticipant(Participant participant);

    boolean existsByUserIdAndVoteId(@Param("userId") String userId, @Param("voteId") Integer voteId);

//    @Delete("DELETE FROM participant WHERE vote_id = #{voteId} AND user_id = #{userId}")
    void deleteParticipant(@Param("voteId") Integer voteId, @Param("userId") String userId);
    // 투표자 리스트 조회
    List<Participant> findParticipantsByVoteId(@Param("voteId") Integer voteId);
}

