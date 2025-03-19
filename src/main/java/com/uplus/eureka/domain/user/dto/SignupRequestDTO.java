package com.uplus.eureka.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SignupRequestDTO {
    private String userId;
    private String userName;
    private String userPwd;
    private String emailId;
    private String emailDomain;
}