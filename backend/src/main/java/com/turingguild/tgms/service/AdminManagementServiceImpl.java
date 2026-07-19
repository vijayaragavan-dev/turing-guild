package com.turingguild.tgms.service;

import com.turingguild.tgms.dto.request.AdminResetPasswordRequest;
import com.turingguild.tgms.dto.request.CreateAdminRequest;
import com.turingguild.tgms.dto.request.UpdateAdminRequest;
import com.turingguild.tgms.dto.response.AdminResponse;
import com.turingguild.tgms.entity.User;
import com.turingguild.tgms.entity.enums.Role;
import com.turingguild.tgms.exception.BadRequestException;
import com.turingguild.tgms.exception.ResourceNotFoundException;
import com.turingguild.tgms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminManagementServiceImpl implements AdminManagementService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AdminResponse createAdmin(CreateAdminRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new BadRequestException("Email already registered");
        }

        User admin = User.builder()
                .fullName(request.getFullName().trim())
                .email(normalizedEmail)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .firstLogin(false)
                .build();

        User saved = userRepository.save(admin);
        log.info("New admin created: {}", normalizedEmail);
        return toResponse(saved);
    }

    @Override
    public List<AdminResponse> getAllAdmins() {
        return userRepository.findByRole(Role.ADMIN).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public AdminResponse getAdminById(Long id) {
        User user = userRepository.findById(id)
                .filter(u -> u.getRole() == Role.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
        return toResponse(user);
    }

    @Override
    @Transactional
    public AdminResponse updateAdmin(Long id, UpdateAdminRequest request) {
        User user = userRepository.findById(id)
                .filter(u -> u.getRole() == Role.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        if (request.getEmail() != null) {
            String normalizedEmail = request.getEmail().trim().toLowerCase();
            if (!normalizedEmail.equals(user.getEmail()) && userRepository.existsByEmail(normalizedEmail)) {
                throw new BadRequestException("Email already registered");
            }
            user.setEmail(normalizedEmail);
        }

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName().trim());
        }

        if (request.getIsActive() != null) {
            user.setIsActive(request.getIsActive());
        }

        User saved = userRepository.save(user);
        log.info("Admin updated: {}", saved.getEmail());
        return toResponse(saved);
    }

    @Override
    @Transactional
    public AdminResponse updateAdminStatus(Long id, Boolean isActive) {
        User user = userRepository.findById(id)
                .filter(u -> u.getRole() == Role.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        user.setIsActive(isActive);
        User saved = userRepository.save(user);
        log.info("Admin {} status updated to: {}", saved.getEmail(), isActive ? "ACTIVE" : "INACTIVE");
        return toResponse(saved);
    }

    @Override
    @Transactional
    public void resetPassword(Long id, AdminResetPasswordRequest request) {
        User user = userRepository.findById(id)
                .filter(u -> u.getRole() == Role.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setFirstLogin(false);
        userRepository.save(user);
        log.info("Password reset for admin: {}", user.getEmail());
    }

    @Override
    @Transactional
    public void deleteAdmin(Long id) {
        User user = userRepository.findById(id)
                .filter(u -> u.getRole() == Role.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        user.setIsActive(false);
        userRepository.save(user);
        log.info("Admin soft-deleted (deactivated): {}", user.getEmail());
    }

    private AdminResponse toResponse(User user) {
        return AdminResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .isActive(user.getIsActive())
                .firstLogin(user.getFirstLogin())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
