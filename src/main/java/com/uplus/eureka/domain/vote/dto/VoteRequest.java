package com.uplus.eureka.domain.vote.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VoteRequest{
    private Integer voteId;  // 투표 ID (수정 시 필요)
    private String creatorId; 
    private String title;
    private String description; 
    private LocalDateTime deadline;
    private LocalDateTime meetingStartTime;
    private LocalDateTime meetingEndTime;
    private Integer recruit;
    private String restaurantName;  // 가게명 추가
    private Double latitude;        // 가게 위도 추가
    private Double longitude;       // 가게 경도 추가
    }