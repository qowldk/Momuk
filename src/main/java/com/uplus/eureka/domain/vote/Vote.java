package com.uplus.eureka.domain.vote;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Vote {
    private Integer voteId;           // 투표 ID
    private String title;             // 투표 제목
    private String description;       // 투표 내용
    private LocalDateTime deadline;   // 투표 마감 시간
    private LocalDateTime meetingStartTime; // 약속 시작 시간
    private LocalDateTime meetingEndTime;   // 약속 종료 시간
    private Integer recruit;          // 모집 인원
    private Integer participants;     // 참여 인원
    private String status;            // 상태 (active, closed)
    private LocalDateTime createdAt;  // 생성일자
    private String restaurantName;    // 가게명
    private Double latitude;          // 가게 위도
    private Double longitude;         // 가게 경도
}
