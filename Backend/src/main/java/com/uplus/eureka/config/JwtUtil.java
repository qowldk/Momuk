package com.uplus.eureka.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import io.jsonwebtoken.ExpiredJwtException; 


@Component
@Slf4j  
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    
    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpiration;
    
    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    private Key getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String userId) {
        return createToken(userId, accessTokenExpiration);
    }

    public String createRefreshToken(String userId) {
        return createToken(userId, refreshTokenExpiration);
    }

    private String createToken(String userId, long expiration) {
        Claims claims = Jwts.claims();
        claims.put("userId", userId);
        
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean checkToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);  // 토큰을 파싱하면서 예외가 발생할 경우 처리
            return true;
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰: {}", e.getMessage());  // 만료된 토큰 로그
            return false;
        } catch (Exception e) {
            log.error("토큰 검증 실패임: {}", e.getMessage());  // 일반적인 예외 로그
            return false;
        }
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return claims.get("userId", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);  // 수정된 부분
            return true;
        } catch (ExpiredJwtException e) {
            log.error("JWT 토큰이 만료되었습니다.");
        } catch (Exception e) {
            log.error("유효하지 않은 JWT 토큰입니다.");
        }
        return false;
    }



}
