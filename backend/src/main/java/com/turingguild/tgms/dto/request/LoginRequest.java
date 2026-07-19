package com.turingguild.tgms.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Username/email is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    public String getUsername() {
        return username != null ? username.trim() : null;
    }
}
