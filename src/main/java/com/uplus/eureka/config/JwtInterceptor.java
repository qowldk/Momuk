package com.uplus.eureka.config;

import com.uplus.eureka.domain.user.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    private static final String HEADER_AUTH = "Authorization";
    private final JwtUtil jwtUtil;

    public JwtInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();

        // ✅ Swagger 관련 경로는 인증 예외 처리
        if (uri.contains("swagger") || uri.contains("api-docs")) {
            return true;
        }

        // 기존 자동 마감, 상태 조회 예외 처리도 함께
        if (uri.equals("/api/vote/auto-close") || uri.startsWith("/api/vote/status")) {
            return true;
        }

        // 로그아웃 경로는 인증 예외 처리 (Authorization 헤더 없이도 허용)
        if (uri.equals("/api/users/logout")) {
            return true;
        }

        String authHeader = request.getHeader(HEADER_AUTH);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Authorization 헤더가 없거나 잘못됨. 요청 URI: {}, 헤더 값: {}", uri, authHeader);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("401 Unauthorized: 토큰이 필요합니다.");
            return false;
        }

        String token = authHeader.replace("Bearer ", "").trim();

        try {
            if (jwtUtil.checkToken(token)) {
                log.info("유효한 토큰: {}", token);
                return true;
            } else {
                throw new UnauthorizedException("유효하지 않은 토큰입니다.");
            }
        } catch (Exception e) {
            log.error("토큰 검증 중 오류: {}", e.getMessage());
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }
    }
}