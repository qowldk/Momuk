package com.uplus.eureka.domain.user.controller;

import com.uplus.eureka.config.JwtUtil;
import com.uplus.eureka.domain.user.User;
import com.uplus.eureka.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "사용자 인증 컨트롤러", description = "로그인, 회원가입, 토큰처리 등 회원의 인증관련 처리하는 클래스")
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "회원가입", description = "새로운 사용자 정보를 등록합니다.")
    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(
            @RequestBody @Parameter(description = "회원가입 시 필요한 회원정보", required = true) User user) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.CREATED;
        
        try {
            userService.signup(user);
            resultMap.put("message", "회원가입이 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            log.error("회원가입 에러 발생 : {}", e.getMessage());
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        
        return new ResponseEntity<>(resultMap, status);
    }

    
}
