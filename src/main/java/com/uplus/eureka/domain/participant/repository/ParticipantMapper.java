package com.uplus.eureka.domain.participant.repository;

import org.apache.ibatis.annotations.Param;
import com.uplus.eureka.domain.participant.Participant;

public interface ParticipantMapper {
    void insertParticipant(Participant participant);
    
    boolean existsByUserIdAndVoteId(@Param("userId") String userId, @Param("voteId") Integer voteId);
}
