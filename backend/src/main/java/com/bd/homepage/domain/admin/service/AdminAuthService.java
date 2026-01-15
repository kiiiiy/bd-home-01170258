package com.bd.homepage.domain.admin.service;

import com.bd.homepage.domain.admin.controller.AdminAuthController.AdminLoginRequest;
import com.bd.homepage.domain.admin.controller.AdminAuthController.AdminLoginResponse;
import com.bd.homepage.global.exception.UnauthorizedException;
import com.bd.homepage.global.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthService {

    private final String adminPasswordHash;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminAuthService(
            @Value("${admin.password-hash}") String adminPasswordHash,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.adminPasswordHash = adminPasswordHash;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AdminLoginResponse login(AdminLoginRequest req) {
        if (req == null || req.password() == null || req.password().isBlank()) {
            throw new UnauthorizedException("PASSWORD_REQUIRED");
        }

        if (!passwordEncoder.matches(req.password(), adminPasswordHash)) {
            throw new UnauthorizedException("INVALID_PASSWORD");
        }

        String token = jwtTokenProvider.createAdminToken();
        return new AdminLoginResponse(token, jwtTokenProvider.expiresSeconds());
    }
}
