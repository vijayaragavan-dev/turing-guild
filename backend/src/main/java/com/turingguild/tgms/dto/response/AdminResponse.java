package com.turingguild.tgms.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AdminResponse {
    private Long id;
    private String fullName;
    private String email;
    private Boolean isActive;
    private Boolean firstLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
