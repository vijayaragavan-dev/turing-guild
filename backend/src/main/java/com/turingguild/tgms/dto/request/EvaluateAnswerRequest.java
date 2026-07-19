package com.turingguild.tgms.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EvaluateAnswerRequest {

    @NotNull(message = "Answer ID is required")
    private Long answerId;

    @NotNull(message = "Score is required")
    private Integer scoreAwarded;

    private String feedback;
}
