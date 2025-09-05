package com.uplus.eureka.domain.participant;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Participant {
    private Integer participantId; // 참여자 ID
    private String userId;        // 사용자 ID (FK)
    private Integer voteId;        // 투표글 ID (FK)
    private LocalDateTime createdAt; // 참여 일자
    private String userName;
}
