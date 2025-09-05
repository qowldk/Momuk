package com.uplus.eureka.domain.vote;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Vote {
    private Integer voteId;           
    private String creatorId; 
    private String title;             
    private String description;       
    private LocalDateTime deadline;   
    private LocalDateTime meetingStartTime; // 약속 시작 시간
    private LocalDateTime meetingEndTime;   // 약속 종료 시간
    private Integer recruit;          // 모집 인원
    private Integer participants;     // 참여 인원
    private String status;            // 상태 (active, closed)
    private LocalDateTime createdAt;  
    private String restaurantName;    
    private Double latitude;          
    private Double longitude;         

}
