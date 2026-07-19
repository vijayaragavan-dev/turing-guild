package com.turingguild.tgms.dto.response;

import com.turingguild.tgms.entity.enums.QuestionType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuestionResponse {
    private Long id;
    private QuestionType questionType;
    private String questionText;
    private Integer marks;
    private Integer orderIndex;
    private String codingTemplate;
    private String expectedOutput;
    private String sampleInput;
    private String sampleOutput;
    private List<OptionResponse> options;
}
