package com.turingguild.tgms.controller;

import com.turingguild.tgms.dto.request.AdminResetPasswordRequest;
import com.turingguild.tgms.dto.request.CreateAdminRequest;
import com.turingguild.tgms.dto.request.UpdateAdminRequest;
import com.turingguild.tgms.dto.response.AdminResponse;
import com.turingguild.tgms.dto.response.MessageResponse;
import com.turingguild.tgms.service.AdminManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Management", description = "Admin account management endpoints")
public class AdminManagementController {

    private final AdminManagementService adminManagementService;

    @PostMapping
    @Operation(summary = "Create a new admin account")
    public ResponseEntity<AdminResponse> createAdmin(@Valid @RequestBody CreateAdminRequest request) {
        return ResponseEntity.ok(adminManagementService.createAdmin(request));
    }

    @GetMapping
    @Operation(summary = "List all admin accounts")
    public ResponseEntity<List<AdminResponse>> getAllAdmins() {
        return ResponseEntity.ok(adminManagementService.getAllAdmins());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get admin by ID")
    public ResponseEntity<AdminResponse> getAdminById(@PathVariable Long id) {
        return ResponseEntity.ok(adminManagementService.getAdminById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update admin account")
    public ResponseEntity<AdminResponse> updateAdmin(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAdminRequest request) {
        return ResponseEntity.ok(adminManagementService.updateAdmin(id, request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Activate or deactivate admin")
    public ResponseEntity<AdminResponse> updateAdminStatus(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, Boolean> body) {
        Boolean isActive = body.get("isActive");
        if (isActive == null) {
            throw new com.turingguild.tgms.exception.BadRequestException("isActive field is required");
        }
        return ResponseEntity.ok(adminManagementService.updateAdminStatus(id, isActive));
    }

    @PatchMapping("/{id}/reset-password")
    @Operation(summary = "Reset admin password")
    public ResponseEntity<MessageResponse> resetPassword(
            @PathVariable Long id,
            @Valid @RequestBody AdminResetPasswordRequest request) {
        adminManagementService.resetPassword(id, request);
        return ResponseEntity.ok(new MessageResponse("Password reset successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete (deactivate) admin")
    public ResponseEntity<MessageResponse> deleteAdmin(@PathVariable Long id) {
        adminManagementService.deleteAdmin(id);
        return ResponseEntity.ok(new MessageResponse("Admin deactivated successfully"));
    }
}
