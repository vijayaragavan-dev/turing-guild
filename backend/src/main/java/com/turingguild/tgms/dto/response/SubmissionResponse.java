package com.turingguild.tgms.dto.response;

import com.turingguild.tgms.entity.enums.SubmissionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SubmissionResponse {
    private Long id;
    private Long userId;
    private String userBatchNumber;
    private Long eventId;
    private String eventTitle;
    private SubmissionStatus status;
    private Integer totalScore;
    private LocalDateTime submittedAt;
    private LocalDateTime createdAt;
    private List<AnswerResponse> answers;
}
