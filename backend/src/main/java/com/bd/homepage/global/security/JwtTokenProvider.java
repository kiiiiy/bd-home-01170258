package com.bd.homepage.global.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private final String secret;
    private final long expiresSeconds;
    private final String issuer;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expires-seconds}") long expiresSeconds,
            @Value("${jwt.issuer}") String issuer
    ) {
        this.secret = secret;
        this.expiresSeconds = expiresSeconds;
        this.issuer = issuer;
    }

    public long expiresSeconds() {
        return expiresSeconds;
    }

    public String createAdminToken() {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expiresSeconds);

        var key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .issuer(issuer)
                .subject("admin")
                .claims(Map.of("role", "ADMIN"))
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key)
                .compact();
    }

    public io.jsonwebtoken.Claims parseClaims(String token) {
        var key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
