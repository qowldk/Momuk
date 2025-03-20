package com.uplus.eureka.config;

import com.uplus.eureka.domain.user.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Enumeration;

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

//        //  자동 마감 API는 JWT 검증 제외
//        if (request.getRequestURI().equals("/api/vote/auto-close")) {
//            log.info("🚀 자동 마감 API 요청 - JWT 검증 제외");
//            return true;  // 인증 없이 요청 통과
//        }
//
//        // 투표 상태 조회 JWT 인증이 필요 없는 API 경로 설정
//        if (request.getRequestURI().startsWith("/api/vote/status") || request.getRequestURI().startsWith("/api/vote/auto-close")) {
//            log.info("JWT 인증이 필요 없는 API 요청: {}", request.getRequestURI());
//            return true;  // 인증 없이 통과
//        }
//
//        // ✅ Authorization 헤더가 없는 경우 처리
//        if (request.getHeader(HEADER_AUTH) == null || request.getHeader(HEADER_AUTH).isEmpty()) {
//            log.warn("Authorization 헤더가 없습니다. 요청 URI: {}", request.getRequestURI());
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("401 Unauthorized: 토큰이 필요합니다.");
//            return false;
//        }

    	final String token = request.getHeader(HEADER_AUTH).replace("Bearer ", "").trim();
        log.info("요청 URL: {}", request.getRequestURI());
        log.info("받은 Authorization 헤더: {}", token);

        if (token == null || token.isEmpty()) {
            log.warn("Authorization 헤더가 존재하지 않음");
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }


        try {
            if (jwtUtil.checkToken(token)) {
                log.info("토큰 사용 가능 : {}", token);
                return true;
            } else {
                log.warn("토큰 검증 실패 : {}", token);
                throw new UnauthorizedException("유효하지 않은 토큰입니다.");
            }
        } catch (Exception e) {
            log.error("토큰 검증 중 예외 발생: {}", e.getMessage());
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }
    }

}
