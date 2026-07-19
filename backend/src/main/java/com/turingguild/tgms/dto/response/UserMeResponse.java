package com.turingguild.tgms.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserMeResponse {
    private Long id;
    private String fullName;
    private String email;
    private String batchNumber;
    private String role;
    private Boolean isActive;
    private Boolean firstLogin;
}
