package com.uplus.eureka.domain.vote.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VoteRequest{
    private String title;
    private String description; 
    private LocalDateTime deadline;
    private LocalDateTime meetingStartTime;
    private LocalDateTime meetingEndTime;
    private Integer recruit;

    }

