package com.turingguild.tgms.service;

import com.turingguild.tgms.dto.request.AdminRegisterRequest;
import com.turingguild.tgms.dto.request.ChangePasswordRequest;
import com.turingguild.tgms.dto.request.LoginRequest;
import com.turingguild.tgms.dto.response.AuthResponse;
import com.turingguild.tgms.dto.response.UserMeResponse;
import com.turingguild.tgms.entity.User;
import com.turingguild.tgms.entity.enums.Role;
import com.turingguild.tgms.exception.BadRequestException;
import com.turingguild.tgms.exception.UnauthorizedException;
import com.turingguild.tgms.repository.UserRepository;
import com.turingguild.tgms.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        User user = userRepository.findByBatchNumberOrEmail(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (!user.getIsActive()) {
            throw new UnauthorizedException("Account has been deactivated");
        }

        log.info("User logged in: {}", user.getBatchNumber() != null ? user.getBatchNumber() : user.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .requiresPasswordChange(user.getFirstLogin())
                .role(user.getRole().name())
                .username(user.getBatchNumber() != null ? user.getBatchNumber() : user.getEmail())
                .build();
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new BadRequestException("Invalid refresh token");
        }

        String username = tokenProvider.getUsernameFromToken(refreshToken);
        String newAccessToken = tokenProvider.generateAccessToken(username);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void changePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByBatchNumberOrEmail(username, username)
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Current password is incorrect");
        }

        if (request.getCurrentPassword().equals(request.getNewPassword())) {
            throw new BadRequestException("New password must be different from current password");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setFirstLogin(false);
        userRepository.save(user);

        log.info("Password changed for user: {}", username);
    }

    @Transactional
    public AuthResponse registerAdmin(AdminRegisterRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new UnauthorizedException("Only authenticated admins can create admin accounts");
        }

        String normalizedEmail = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new BadRequestException("Email already registered");
        }

        User admin = User.builder()
                .fullName(request.getFullName().trim())
                .email(normalizedEmail)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .isActive(true)
                .firstLogin(true)
                .build();

        userRepository.save(admin);
        log.info("New admin created by {}: {}", auth.getName(), normalizedEmail);

        return AuthResponse.builder()
                .role(Role.ADMIN.name())
                .username(normalizedEmail)
                .build();
    }

    public UserMeResponse getCurrentUser(String username) {
        User user = userRepository.findByBatchNumberOrEmail(username, username)
                .orElseThrow(() -> new BadRequestException("User not found"));

        return UserMeResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .batchNumber(user.getBatchNumber())
                .role(user.getRole().name())
                .isActive(user.getIsActive())
                .firstLogin(user.getFirstLogin())
                .build();
    }

}
