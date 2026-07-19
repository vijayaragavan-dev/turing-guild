package com.turingguild.tgms.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class EvaluateSubmissionRequest {

    @NotEmpty(message = "Evaluation cannot be empty")
    private List<EvaluateAnswerRequest> answers;
}
