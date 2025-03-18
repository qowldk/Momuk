package com.uplus.eureka.domain.user.repository;

import com.uplus.eureka.domain.user.User;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.Map;

@Mapper
public interface UserMapper {
    User login(User user) throws SQLException;
    void signup(User user) throws SQLException;
    User getUserById(String userId) throws SQLException;
    void saveRefreshToken(Map<String, Object> map) throws SQLException;
    String getRefreshToken(String userId) throws SQLException;
    void deleteRefreshToken(Map<String, Object> map) throws SQLException;
}
