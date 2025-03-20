package com.uplus.eureka.domain.participant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParticipantResponse {
    private String userId;
    private String userName;
}