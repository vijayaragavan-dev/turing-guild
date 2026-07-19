package com.turingguild.tgms.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateAdminRequest {

    @Size(min = 3, max = 255, message = "Full name must be between 3 and 255 characters")
    private String fullName;

    @Email(message = "A valid email is required")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    private Boolean isActive;
}
