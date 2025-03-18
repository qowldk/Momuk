package com.uplus.eureka.domain.user.service;

import com.uplus.eureka.domain.user.User;
import com.uplus.eureka.domain.user.repository.UserMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User login(User user) throws Exception {
        return userMapper.login(user);
    }

    @Override
    public void signup(User user) throws Exception {
        userMapper.signup(user);
    }

    @Override
    public User getUserById(String userId) throws Exception {
        return userMapper.getUserById(userId);
    }

    @Override
    public void saveRefreshToken(String userId, String refreshToken) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("token", refreshToken);
        userMapper.saveRefreshToken(map);
    }

    @Override
    public String getRefreshToken(String userId) throws Exception {
        return userMapper.getRefreshToken(userId);
    }

    @Override
    public void deleteRefreshToken(String userId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("token", null);
        userMapper.deleteRefreshToken(map);
    }
}
