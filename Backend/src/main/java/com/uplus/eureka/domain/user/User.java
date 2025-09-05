package com.uplus.eureka.domain.user;

import com.uplus.eureka.domain.user.dto.SignupRequestDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {
    private String userId;
    private String userName;
    private String userPwd;
    private String emailId;
    private String emailDomain;
    private String joinDate;
    private String refreshToken;

    // SignupRequestDTO를 받아서 User 객체를 생성하는 생성자 추가
    public User(SignupRequestDTO signupRequestDTO) {
        this.userId = signupRequestDTO.getUserId();
        this.userName = signupRequestDTO.getUserName();
        this.userPwd = signupRequestDTO.getUserPwd();
        this.emailId = signupRequestDTO.getEmailId();
        this.emailDomain = signupRequestDTO.getEmailDomain();
    }
}
