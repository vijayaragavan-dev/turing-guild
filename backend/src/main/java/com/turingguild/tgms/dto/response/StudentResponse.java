package com.turingguild.tgms.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StudentResponse {
    private Long id;
    private String batchNumber;
    private String email;
    private String fullName;
    private Boolean isActive;
    private Boolean firstLogin;
    private LocalDateTime createdAt;
}
