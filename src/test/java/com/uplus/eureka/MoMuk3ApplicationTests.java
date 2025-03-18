package com.uplus.eureka;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class MoMuk3ApplicationTests {

    private static final String SECRET_KEY = "mySecretKey"; // 테스트용 비밀 키

    @Test
    public void testCreateAndParseJwt() {
        // JWT 생성
        String jwt = Jwts.builder()
                .setSubject("testUser") // 사용자 정보
                .setIssuedAt(new Date()) // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 만료 시간 (1시간 후)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 서명 알고리즘 및 비밀 키
                .compact();

        // 생성된 JWT를 출력 (디버깅용)
        System.out.println("Generated JWT: " + jwt);

        // JWT 파싱
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY) // 서명 키 설정
                .parseClaimsJws(jwt) // JWT 파싱
                .getBody();

        // 파싱된 클레임에서 사용자 정보 가져오기
        String subject = claims.getSubject();
        Date expiration = claims.getExpiration();

        // Assertions
        assertNotNull(jwt, "JWT should not be null");
        assertEquals("testUser", subject, "Subject should match");
        assertTrue(expiration.after(new Date()), "Expiration time should be in the future");
    }
}
