package com.turingguild.tgms.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private Boolean requiresPasswordChange;
    private String role;
    private String username;
}
