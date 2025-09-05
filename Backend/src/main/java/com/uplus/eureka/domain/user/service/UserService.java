package com.uplus.eureka.domain.user.service;

import com.uplus.eureka.domain.user.dto.LoginRequestDTO;
import com.uplus.eureka.domain.user.dto.SignupRequestDTO;
import com.uplus.eureka.domain.user.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO login(LoginRequestDTO loginRequestDTO) throws Exception;
    void signup(SignupRequestDTO signupRequestDTO) throws Exception;
    UserResponseDTO getUserById(String userId) throws Exception;
    void saveRefreshToken(String userId, String refreshToken) throws Exception;
    String getRefreshToken(String userId) throws Exception;
    void deleteRefreshToken(String userId) throws Exception;
}
