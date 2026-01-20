package com.farmlink.security;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.expiration.time}")
    private long jwtExpirationTime;

    @Value("${jwt.secret}")
    private String jwtSecret;

    // Symmetric secret key for signing & verification
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        // ❌ Never log secret value
        log.info("Initializing JWT utils (expiration={} ms)", jwtExpirationTime);
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Generate JWT token after successful authentication
     */
    public String generateToken(UserPrincipal principal) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationTime);

        return Jwts.builder()
                .subject(principal.getEmail())          // subject = email
                .issuedAt(now)
                .expiration(expiryDate)
                // custom claims
                .claims(Map.of(
                        "user_id", String.valueOf(principal.getUserId()),
                        "user_role", principal.getUserRole()
                ))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Validate JWT token and return claims
     */
    public Claims validateToken(String jwt) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}
