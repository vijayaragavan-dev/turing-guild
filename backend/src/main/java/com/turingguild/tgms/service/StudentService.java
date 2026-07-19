package com.turingguild.tgms.service;

import com.turingguild.tgms.dto.request.CreateStudentRequest;
import com.turingguild.tgms.dto.request.UpdateStudentRequest;
import com.turingguild.tgms.dto.response.StudentResponse;
import com.turingguild.tgms.entity.User;
import com.turingguild.tgms.entity.enums.Role;
import com.turingguild.tgms.exception.BadRequestException;
import com.turingguild.tgms.exception.ResourceNotFoundException;
import com.turingguild.tgms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<StudentResponse> getAllStudents() {
        return userRepository.findByRole(Role.STUDENT).stream()
                .map(this::toResponse)
                .toList();
    }

    public StudentResponse getStudentById(Long id) {
        User user = userRepository.findById(id)
                .filter(u -> u.getRole() == Role.STUDENT)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return toResponse(user);
    }

    @Transactional
    public StudentResponse createStudent(CreateStudentRequest request) {
        if (userRepository.existsByBatchNumber(request.getBatchNumber())) {
            throw new BadRequestException("Batch number already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        User user = User.builder()
                .batchNumber(request.getBatchNumber())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .isActive(true)
                .firstLogin(true)
                .build();

        return toResponse(userRepository.save(user));
    }

    @Transactional
    public StudentResponse updateStudent(Long id, UpdateStudentRequest request) {
        User user = userRepository.findById(id)
                .filter(u -> u.getRole() == Role.STUDENT)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getIsActive() != null) user.setIsActive(request.getIsActive());

        return toResponse(userRepository.save(user));
    }

    @Transactional
    public void resetPassword(Long id) {
        User user = userRepository.findById(id)
                .filter(u -> u.getRole() == Role.STUDENT)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        String name = user.getFullName() != null ? user.getFullName().trim() : "";
        String batch = user.getBatchNumber() != null ? user.getBatchNumber().trim() : "";
        String prefix = name.length() >= 4 ? name.substring(0, 4) : name;
        String defaultPassword = prefix + batch + "$";

        user.setPasswordHash(passwordEncoder.encode(defaultPassword));
        user.setFirstLogin(true);
        userRepository.save(user);
    }

    @Transactional
    public void deleteStudent(Long id) {
        User user = userRepository.findById(id)
                .filter(u -> u.getRole() == Role.STUDENT)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        user.setIsActive(false);
        userRepository.save(user);
    }

    private StudentResponse toResponse(User user) {
        return StudentResponse.builder()
                .id(user.getId())
                .batchNumber(user.getBatchNumber())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .isActive(user.getIsActive())
                .firstLogin(user.getFirstLogin())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
