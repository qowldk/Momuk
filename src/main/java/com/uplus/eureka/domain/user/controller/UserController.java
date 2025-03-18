//package com.uplus.eureka.domain.user.controller;
//
//import com.uplus.eureka.config.JwtUtil;
//import com.uplus.eureka.domain.user.User;
//import com.uplus.eureka.domain.user.service.UserService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/users")
//@Tag(name = "사용자 인증 컨트롤러", description = "로그인, 회원가입, 토큰처리 등 회원의 인증관련 처리하는 클래스")
//@Slf4j
//public class UserController {
//
//    private final UserService userService;
//    private final JwtUtil jwtUtil;
//
//    public UserController(UserService userService, JwtUtil jwtUtil) {
//        this.userService = userService;
//        this.jwtUtil = jwtUtil;
//    }
//
//    @Operation(summary = "회원가입", description = "새로운 사용자 정보를 등록합니다.")
//    @PostMapping("/signup")
//    public ResponseEntity<Map<String, Object>> signup(
//            @RequestBody @Parameter(description = "회원가입 시 필요한 회원정보", required = true) User user) {
//        Map<String, Object> resultMap = new HashMap<>();
//        HttpStatus status = HttpStatus.CREATED;
//        
//        try {
//            userService.signup(user);
//            resultMap.put("message", "회원가입이 성공적으로 완료되었습니다.");
//        } catch (Exception e) {
//            log.error("회원가입 에러 발생 : {}", e.getMessage());
//            resultMap.put("message", e.getMessage());
//            status = HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        
//        return new ResponseEntity<>(resultMap, status);
//    }
//    
//    @Operation(summary = "로그인", description = "아이디와 비밀번호를 이용하여 로그인 처리합니다.")
//    @PostMapping("/login")
//    public ResponseEntity<Map<String, Object>> login(
//            @RequestBody @Parameter(description = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true) User user) {
//        log.debug("login user : {}", user);
//        Map<String, Object> resultMap = new HashMap<>();
//        HttpStatus status = HttpStatus.ACCEPTED;
//        
//        try {
//            User loginUser = userService.login(user);
//            
//            if (loginUser != null) {
//                String accessToken = jwtUtil.createAccessToken(loginUser.getUserId());
//                String refreshToken = jwtUtil.createRefreshToken(loginUser.getUserId());
//                
//                log.debug("access token : {}", accessToken);
//                log.debug("refresh token : {}", refreshToken);
//                
//                // 발급받은 refresh token을 DB에 저장
//                userService.saveRefreshToken(loginUser.getUserId(), refreshToken);
//                
//                resultMap.put("access-token", accessToken);
//                resultMap.put("refresh-token", refreshToken);
//                status = HttpStatus.CREATED;
//            } else {
//                resultMap.put("message", "아이디 또는 패스워드를 확인해 주세요.");
//                status = HttpStatus.UNAUTHORIZED;
//            }
//        } catch (Exception e) {
//            log.debug("로그인 에러 발생 : {}", e);
//            resultMap.put("message", e.getMessage());
//            status = HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        
//        return new ResponseEntity<>(resultMap, status);
//    }
//}
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
    
    @Operation(summary = "로그인", description = "아이디와 비밀번호를 이용하여 로그인 처리합니다.")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody @Parameter(description = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true) User user) {
        log.debug("login user : {}", user);
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        
        try {
            User loginUser = userService.login(user);
            
            if (loginUser != null) {
                String accessToken = jwtUtil.createAccessToken(loginUser.getUserId());
                String refreshToken = jwtUtil.createRefreshToken(loginUser.getUserId());
                
                log.debug("access token : {}", accessToken);
                log.debug("refresh token : {}", refreshToken);
                
                // 발급받은 refresh token을 DB에 저장
                userService.saveRefreshToken(loginUser.getUserId(), refreshToken);
                
                resultMap.put("access-token", accessToken);
                resultMap.put("refresh-token", refreshToken);
                status = HttpStatus.CREATED;
            } else {
                resultMap.put("message", "아이디 또는 패스워드를 확인해 주세요.");
                status = HttpStatus.UNAUTHORIZED;
            }
        } catch (Exception e) {
            log.debug("로그인 에러 발생 : {}", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        
        return new ResponseEntity<>(resultMap, status);
    }
    
    @Operation(summary = "Access Token 재발급", description = "유효한 Refresh Token을 이용하여 새로운 Access Token을 재발급합니다.")
    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, Object>> refreshAccessToken(
            @RequestHeader("Authorization") String refreshTokenHeader) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        
        try {
            // Authorization 헤더에서 'Bearer ' 제거
            String refreshToken = refreshTokenHeader.replace("Bearer ", "").trim();

            // Refresh Token 검증
            if (!jwtUtil.validateToken(refreshToken)) {
                resultMap.put("message", "유효하지 않거나 만료된 Refresh Token입니다.");
                status = HttpStatus.UNAUTHORIZED;
                return new ResponseEntity<>(resultMap, status);
            }

            // 사용자 ID 추출
            String userId = jwtUtil.getUserIdFromToken(refreshToken);

            // 새로운 Access Token 생성
            String newAccessToken = jwtUtil.createAccessToken(userId);

            resultMap.put("access-token", newAccessToken);
            status = HttpStatus.OK;
        } catch (Exception e) {
            log.error("Refresh token 처리 중 오류 발생: {}", e.getMessage());
            resultMap.put("message", "Refresh Token 처리 중 오류가 발생했습니다.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        
        return new ResponseEntity<>(resultMap, status);
    }
}

