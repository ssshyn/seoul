package com.sm.seoulmate.domain.login.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey secretKey = Keys.hmacShaKeyFor("api-seoulmate-jwt-authentication-sha256-login-secret-key".getBytes(StandardCharsets.UTF_8));

    public String generateAccessToken(String userId) {
        // 15분
        long accessTokenValidity = 1000 * 60 * 15;
        return generateToken(userId, accessTokenValidity);
    }

    public String generateRefreshToken(String userId) {
        // 7일
        long refreshTokenValidity = 1000 * 60 * 24 * 7;
        return generateToken(userId, refreshTokenValidity);
    }

    public String generateToken(String subject, long expirationMillis) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)  // secretKey를 설정
                    .build()  // 빌드하여 파서 객체 생성
                    .parseClaimsJws(token)  // 토큰 파싱
                    .getBody();
        } catch (JwtException e) {
            throw new RuntimeException("Invalid token", e);
        }
    }
}
