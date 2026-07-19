package com.turingguild.tgms.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateStudentRequest {

    @Email(message = "Valid email is required")
    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String fullName;

    private Boolean isActive;
}
