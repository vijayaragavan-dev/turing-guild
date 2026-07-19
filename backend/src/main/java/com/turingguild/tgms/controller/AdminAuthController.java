package com.turingguild.tgms.controller;

import com.turingguild.tgms.dto.request.AdminRegisterRequest;
import com.turingguild.tgms.dto.response.AuthResponse;
import com.turingguild.tgms.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Management", description = "Admin account management endpoints (ADMIN role required)")
public class AdminAuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
        summary = "Create admin account",
        description = "Create a new admin account. Only accessible to authenticated ADMIN users."
    )
    public ResponseEntity<AuthResponse> registerAdmin(@Valid @RequestBody AdminRegisterRequest request) {
        return ResponseEntity.ok(authService.registerAdmin(request));
    }
}
