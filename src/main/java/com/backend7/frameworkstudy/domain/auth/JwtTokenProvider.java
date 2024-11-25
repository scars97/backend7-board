package com.backend7.frameworkstudy.domain.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long accessTokenExpirationMinutes;
    private final long refreshTokenExpirationMinutes;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration-minutes}") long accessTokenExpirationMinutes,
            @Value("${jwt.refresh-token-expiration-minutes}") long refreshTokenExpirationMinutes) {
        this.secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessTokenExpirationMinutes = accessTokenExpirationMinutes;
        this.refreshTokenExpirationMinutes = refreshTokenExpirationMinutes;
    }

    public String generateAccessToken(Long id) {
        return Jwts.builder()
                .claims(createClaims(id))
                .subject(String.valueOf(id))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(Date.from(Instant.now().plusSeconds(accessTokenExpirationMinutes * 60)))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(Long id) {
        return Jwts.builder()
                .claims(createClaims(id))
                .subject(String.valueOf(id))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(Date.from(Instant.now().plusSeconds(refreshTokenExpirationMinutes * 60)))
                .signWith(secretKey)
                .compact();
    }

    private Map<String, Object> createClaims(final Long id) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("id", id);

        return claims;
    }

    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.split(" ")[1];
        }
        return null;
    }

    public MemberDetail getMember(String token) {
        Claims claims = verifyToken(token);

        return MemberDetail.builder()
                .id(claims.get("id", Long.class))
                .build();
    }

    public Long getId(String token) {
        Claims claims = verifyToken(token);
        return claims.get("id", Long.class);
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = verifyToken(token);
            log.info("expireTime :{}", claims.getExpiration());
            log.info("memberId : {}", claims.get("id"));
            return true;

        } catch (ExpiredJwtException exception) {
            log.error("Token Expired");
            return false;
        } catch (JwtException exception) {
            log.error("Token Tampered");
            return false;
        } catch (NullPointerException exception) {
            log.error("Token is null");
            return false;
        }
    }

    private Claims verifyToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }
}
