package com.turingguild.tgms.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OptionResponse {
    private Long id;
    private String optionText;
    private Integer orderIndex;
    // isCorrect intentionally omitted from student-facing responses
}
