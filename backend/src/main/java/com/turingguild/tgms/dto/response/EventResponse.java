package com.turingguild.tgms.dto.response;

import com.turingguild.tgms.entity.enums.EventCategory;
import com.turingguild.tgms.entity.enums.EventStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class EventResponse {
    private Long id;
    private String title;
    private String description;
    private EventCategory category;
    private EventStatus status;
    private Integer durationMinutes;
    private Integer totalMarks;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String createdBy;
    private LocalDateTime createdAt;
    private List<QuestionResponse> questions;
}
