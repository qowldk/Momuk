package com.uplus.eureka.domain.user.controller;

import com.uplus.eureka.config.JwtUtil;
import com.uplus.eureka.domain.user.dto.LoginRequestDTO;
import com.uplus.eureka.domain.user.dto.SignupRequestDTO;
import com.uplus.eureka.domain.user.dto.UserResponseDTO;
import com.uplus.eureka.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
            @RequestBody @Parameter(description = "회원가입 시 필요한 회원정보", required = true) SignupRequestDTO signupRequestDTO) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.CREATED;

        try {
            userService.signup(signupRequestDTO);
            resultMap.put("message", "회원가입이 성공적으로 완료되었습니다.");
            log.info("회원가입 성공: {}", signupRequestDTO.getUserId());
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
            @RequestBody @Parameter(description = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true) LoginRequestDTO loginRequestDTO,
            HttpServletResponse response) {

        log.debug("login user : {}", loginRequestDTO);
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            UserResponseDTO userResponseDTO = userService.login(loginRequestDTO);

            if (userResponseDTO != null) {
                String accessToken = jwtUtil.createAccessToken(userResponseDTO.getUserId());
                String refreshToken = jwtUtil.createRefreshToken(userResponseDTO.getUserId());

                log.debug("access token : {}", accessToken);
                log.debug("refresh token : {}", refreshToken);

                // ✅ Set access token as cookie (manually with SameSite=None)
                // refreshToken을 HttpOnly 쿠키로 저장
                String cookieValue = String.format("refreshToken=%s; Max-Age=604800; Path=/; HttpOnly; Secure; SameSite=None", refreshToken);
                response.addHeader("Set-Cookie", cookieValue);

                // ✅ Store refresh token
                userService.saveRefreshToken(userResponseDTO.getUserId(), refreshToken);

                resultMap.put("userInfo", userResponseDTO);
                resultMap.put("access-token", accessToken);
                status = HttpStatus.CREATED;
                log.info("로그인 성공: {}", userResponseDTO.getUserId());
            } else {
                resultMap.put("message", "아이디 또는 패스워드를 확인해 주세요.");
                status = HttpStatus.UNAUTHORIZED;
                log.warn("로그인 실패: 아이디 또는 패스워드 불일치");
            }
        } catch (Exception e) {
            log.error("로그인 에러 발생 : {}", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, Object>> refreshAccessToken(
            @RequestHeader("Authorization") String refreshTokenHeader) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.OK;

        try {
            String refreshToken = refreshTokenHeader.replace("Bearer ", "").trim();

            if (!jwtUtil.validateToken(refreshToken)) {
                resultMap.put("message", "유효하지 않거나 만료된 Refresh Token입니다.");
                status = HttpStatus.UNAUTHORIZED;
                log.warn("유효하지 않은 Refresh Token");
                return new ResponseEntity<>(resultMap, status);
            }

            String userId = jwtUtil.getUserIdFromToken(refreshToken);
            String newAccessToken = jwtUtil.createAccessToken(userId);

            resultMap.put("access-token", newAccessToken);
            status = HttpStatus.OK;
            log.info("Access Token 재발급 성공: {}", userId);
        } catch (Exception e) {
            log.error("Refresh token 처리 중 오류 발생: {}", e.getMessage());
            resultMap.put("message", "Refresh Token 처리 중 오류가 발생했습니다.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/logout")
    @CrossOrigin(
            origins = "http://localhost:5174",
            allowCredentials = "true",
            allowedHeaders = "*",
            methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    public ResponseEntity<Map<String, Object>> logout(
            HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            String token = null;
            Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    log.debug("요청 쿠키: {} = {}", cookie.getName(), cookie.getValue());
                    if ("accessToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                    }
                }
            }

            if (token == null) {
                log.error("accessToken 쿠키가 없습니다.");
                resultMap.put("message", "accessToken 쿠키가 없습니다.");
                return new ResponseEntity<>(resultMap, HttpStatus.UNAUTHORIZED);
            }

            boolean isValid = jwtUtil.checkToken(token);
            log.debug("토큰 유효성 검사 결과: {}", isValid);

            if (!isValid) {
                log.error("토큰이 유효하지 않음 (만료 또는 위조)");
                resultMap.put("message", "유효하지 않은 토큰입니다.");
                return new ResponseEntity<>(resultMap, HttpStatus.UNAUTHORIZED);
            }

            String userId = jwtUtil.getUserIdFromToken(token);
            log.debug("토큰에서 추출한 사용자 ID: {}", userId);

            userService.deleteRefreshToken(userId);

            // ✅ Remove the cookie
            response.addHeader("Set-Cookie", "accessToken=; Max-Age=0; Path=/; HttpOnly; Secure; SameSite=None");

            log.info("로그아웃 성공: {}", userId);
            resultMap.put("message", "로그아웃이 성공적으로 처리되었습니다.");
            status = HttpStatus.OK;

        } catch (Exception e) {
            log.error("로그아웃 처리 중 예외 발생: {}", e.getMessage(), e);
            resultMap.put("message", "로그아웃 처리 중 오류가 발생했습니다.");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @Operation(summary = "회원인증", description = "회원 정보를 담은 Token을 반환합니다.")
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getUserInfo(
            @PathVariable("userId") String userId,
            HttpServletRequest request) {

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtUtil.checkToken(token)) {
                log.info("사용 가능한 토큰!!!");
                try {
                    UserResponseDTO userResponseDTO = userService.getUserById(userId);
                    resultMap.put("userInfo", userResponseDTO);
                    status = HttpStatus.OK;
                } catch (Exception e) {
                    log.error("정보조회 실패 : {}", e);
                    resultMap.put("message", e.getMessage());
                    status = HttpStatus.INTERNAL_SERVER_ERROR;
                }
            } else {
                log.error("사용 불가능 토큰!!!");
                resultMap.put("message", "인증 토큰이 유효하지 않습니다.");
                status = HttpStatus.UNAUTHORIZED;
            }
        } else {
            log.error("토큰이 존재하지 않거나 형식이 잘못되었습니다.");
            resultMap.put("message", "인증 토큰이 필요합니다.");
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(resultMap, status);
    }
}
