package com.turingguild.tgms.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class SubmitAnswersRequest {

    @NotEmpty(message = "Answers cannot be empty")
    private List<AnswerRequest> answers;
}
