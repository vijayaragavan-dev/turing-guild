package com.turingguild.tgms.service;

import com.turingguild.tgms.dto.request.AdminResetPasswordRequest;
import com.turingguild.tgms.dto.request.CreateAdminRequest;
import com.turingguild.tgms.dto.request.UpdateAdminRequest;
import com.turingguild.tgms.dto.response.AdminResponse;

import java.util.List;

public interface AdminManagementService {
    AdminResponse createAdmin(CreateAdminRequest request);
    List<AdminResponse> getAllAdmins();
    AdminResponse getAdminById(Long id);
    AdminResponse updateAdmin(Long id, UpdateAdminRequest request);
    AdminResponse updateAdminStatus(Long id, Boolean isActive);
    void resetPassword(Long id, AdminResetPasswordRequest request);
    void deleteAdmin(Long id);
}
