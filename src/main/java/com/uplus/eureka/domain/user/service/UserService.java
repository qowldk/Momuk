package com.uplus.eureka.domain.user.service;

import com.uplus.eureka.domain.user.User;

public interface UserService {
    User login(User user) throws Exception;
    void signup(User user) throws Exception;
    User getUserById(String userId) throws Exception;
    void saveRefreshToken(String userId, String refreshToken) throws Exception;
    String getRefreshToken(String userId) throws Exception;
    void deleteRefreshToken(String userId) throws Exception;
}
