package com.appswave.newsmanagement.util;

import com.appswave.newsmanagement.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String accessTokenSecret;
    @Value("${jwt.refresh.secret}")
    private String refreshTokenSecret;
    @Value("${jwt.token.validity}")
    private Long accessTokenValidity;
    @Value("${jwt.refresh.token.validity}")
    private Long refreshTokenValidity;

    public String generateAccessToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username, accessTokenValidity, accessTokenSecret);
    }

    public String generateRefreshToken(String username) {
        return createToken(new HashMap<>(), username, refreshTokenValidity, refreshTokenSecret);
    }

    private String createToken(Map<String, Object> claims, String subject, Long validity, String secret) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes()))
                .compact();
    }

    private Claims extractAllClaims(String token, String secret) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsernameFromAccessToken(String token) {
        return extractClaim(token, Claims::getSubject, accessTokenSecret);
    }

    public String extractUsernameFromRefreshToken(String token) {
        return extractClaim(token, Claims::getSubject, refreshTokenSecret);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, String secret) {
        final Claims claims = extractAllClaims(token, secret);
        return claimsResolver.apply(claims);
    }

    public Boolean isAccessTokenExpired(String token) {
        return extractExpiration(token, accessTokenSecret).before(new Date());
    }

    public Boolean isRefreshTokenExpired(String token) {
        return extractExpiration(token, refreshTokenSecret).before(new Date());
    }

    private Date extractExpiration(String token, String secret) {
        return extractClaim(token, Claims::getExpiration, secret);
    }
    public Boolean validateAccessToken(String token, String username) {
        if (Boolean.TRUE.equals(isAccessTokenExpired(token))) {
            throw new TokenExpiredException("Access token has expired");
        }
        final String extractedUsername = extractUsernameFromAccessToken(token);
        return extractedUsername.equals(username);
    }
    public Boolean validateRefreshToken(String token, String username) {
        if (Boolean.TRUE.equals(isRefreshTokenExpired(token))) {
            throw new TokenExpiredException("Refresh token has expired");
        }
        final String extractedUsername = extractUsernameFromRefreshToken(token);
        return extractedUsername.equals(username);
    }
}