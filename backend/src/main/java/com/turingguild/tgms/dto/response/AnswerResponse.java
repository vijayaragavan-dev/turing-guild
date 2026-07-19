package com.turingguild.tgms.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerResponse {
    private Long id;
    private Long questionId;
    private String answerText;
    private Boolean isCorrect;
    private Integer scoreAwarded;
    private String feedback;
}
