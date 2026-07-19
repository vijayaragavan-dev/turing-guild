package com.turingguild.tgms.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnswerRequest {

    @NotNull(message = "Question ID is required")
    private Long questionId;

    private String answerText;
}
