package com.turingguild.tgms.dto.request;

import com.turingguild.tgms.entity.enums.EventCategory;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateEventRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Category is required")
    private EventCategory category;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMinutes;

    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @Future(message = "End time must be in the future")
    private LocalDateTime endTime;
}
