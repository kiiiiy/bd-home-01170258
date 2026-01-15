package com.bd.homepage.domain.admin.controller;

import com.bd.homepage.domain.admin.service.AdminAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    public AdminAuthController(AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponse> login(@RequestBody AdminLoginRequest req) {
        return ResponseEntity.ok(adminAuthService.login(req));
    }

    public record AdminLoginRequest(String password) {}
    public record AdminLoginResponse(String accessToken, long expiresIn) {}
}
