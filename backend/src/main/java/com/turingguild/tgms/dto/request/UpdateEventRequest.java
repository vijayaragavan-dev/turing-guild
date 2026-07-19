package com.turingguild.tgms.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateEventRequest {

    @Size(max = 255)
    private String title;

    private String description;

    private Integer durationMinutes;
}
