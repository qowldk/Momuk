package com.uplus.eureka.domain.user.service;

import com.uplus.eureka.domain.user.User;
import com.uplus.eureka.domain.user.dto.LoginRequestDTO;
import com.uplus.eureka.domain.user.dto.SignupRequestDTO;
import com.uplus.eureka.domain.user.dto.UserResponseDTO;
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
    public UserResponseDTO login(LoginRequestDTO loginRequestDTO) throws Exception {
        User user = new User();
        user.setUserId(loginRequestDTO.getUserId());
        user.setUserPwd(loginRequestDTO.getUserPwd());
        
        // 직접 LoginRequestDTO를 매퍼에 전달
        User loginUser = userMapper.login(loginRequestDTO);
        
        if (loginUser != null) {
            UserResponseDTO userResponseDTO = new UserResponseDTO();
            userResponseDTO.setUserId(loginUser.getUserId());
            userResponseDTO.setUserName(loginUser.getUserName());
            userResponseDTO.setEmailId(loginUser.getEmailId());
            userResponseDTO.setEmailDomain(loginUser.getEmailDomain());
            userResponseDTO.setJoinDate(loginUser.getJoinDate());
            return userResponseDTO;
        }
        return null;
    }

    @Override
    public void signup(SignupRequestDTO signupRequestDTO) throws Exception {
        User user = new User();
        user.setUserId(signupRequestDTO.getUserId());
        user.setUserName(signupRequestDTO.getUserName());
        user.setUserPwd(signupRequestDTO.getUserPwd());
        user.setEmailId(signupRequestDTO.getEmailId());
        user.setEmailDomain(signupRequestDTO.getEmailDomain());
        
        userMapper.signup(signupRequestDTO);
    }

    @Override
    public UserResponseDTO getUserById(String userId) throws Exception {
        User user = userMapper.getUserById(userId);
        
        if (user != null) {
            UserResponseDTO userResponseDTO = new UserResponseDTO();
            userResponseDTO.setUserId(user.getUserId());
            userResponseDTO.setUserName(user.getUserName());
            userResponseDTO.setEmailId(user.getEmailId());
            userResponseDTO.setEmailDomain(user.getEmailDomain());
            userResponseDTO.setJoinDate(user.getJoinDate());
            return userResponseDTO;
        }
        return null;
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
