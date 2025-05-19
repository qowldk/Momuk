package com.uplus.eureka.config;

import com.uplus.eureka.domain.user.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

//@Component
//@Slf4j
//public class JwtInterceptor implements HandlerInterceptor {
//
//    private static final String HEADER_AUTH = "Authorization";
//    private static final int UNAUTHORIZED_STATUS = HttpServletResponse.SC_UNAUTHORIZED;
//
//    private final JwtUtil jwtUtil;
//
//    public JwtInterceptor(JwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//        String uri = request.getRequestURI();
//
//        // 인증 제외 URI 목록
//        if (uri.contains("swagger") || uri.contains("api-docs") ||
//                uri.equals("/api/vote/auto-close") ||
//                uri.startsWith("/api/vote/status") ||
//                uri.equals("/api/users/logout") ||
//                uri.equals("/api/users/refresh-token") ||
//                request.getMethod().equalsIgnoreCase("OPTIONS")) {
//            return true;
//        }
//
//        String authHeader = request.getHeader(HEADER_AUTH);
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            log.warn("Authorization 헤더가 없거나 잘못됨. 요청 URI: {}, 헤더 값: {}", uri, authHeader);
//            response.setStatus(UNAUTHORIZED_STATUS);
//            response.getWriter().write("401 Unauthorized: 토큰이 필요합니다.");
//            return false;
//        }
//
//        String token = authHeader.replace("Bearer ", "").trim();
//
//        if (jwtUtil.checkToken(token)) {
//            log.info("유효한 토큰: {}", token);
//            return true;
//        } else {
//            log.warn("유효하지 않은 토큰: {}", token);
//            response.setStatus(UNAUTHORIZED_STATUS);
//            response.getWriter().write("401 Unauthorized: 유효하지 않은 토큰입니다.");
//            return false;
//        }
//    }
//}
@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    private static final String HEADER_AUTH = "Authorization";
    private static final int UNAUTHORIZED_STATUS = HttpServletResponse.SC_UNAUTHORIZED;

    private final JwtUtil jwtUtil;

    public JwtInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();

        // 인증 제외 URI 목록
        if (uri.contains("swagger") || uri.contains("api-docs") ||
                uri.equals("/api/vote/auto-close") ||
                uri.startsWith("/api/vote/status") ||
                uri.equals("/api/users/logout") ||
                uri.equals("/api/users/refresh-token") ||
                request.getMethod().equalsIgnoreCase("OPTIONS")) {
            return true;
        }

        String authHeader = request.getHeader(HEADER_AUTH);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Authorization 헤더가 없거나 잘못됨. 요청 URI: {}, 헤더 값: {}", uri, authHeader);
            response.setStatus(UNAUTHORIZED_STATUS);
            response.getWriter().write("401 Unauthorized: 토큰이 필요합니다.");
            return false;
        }

        String token = authHeader.replace("Bearer ", "").trim();

        if (jwtUtil.checkToken(token)) {
            log.info("유효한 토큰: {}", token);
            return true;
        } else {
            log.warn("유효하지 않은 토큰: {}", token);
            response.setStatus(UNAUTHORIZED_STATUS);
            response.getWriter().write("401 Unauthorized: 유효하지 않은 토큰입니다.");
            return false;
        }
    }
}