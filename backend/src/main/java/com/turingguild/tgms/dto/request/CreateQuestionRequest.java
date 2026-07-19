package com.turingguild.tgms.dto.request;

import com.turingguild.tgms.entity.enums.QuestionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateQuestionRequest {

    @NotNull(message = "Question type is required")
    private QuestionType questionType;

    @NotBlank(message = "Question text is required")
    private String questionText;

    @Min(value = 1, message = "Marks must be at least 1")
    private Integer marks = 1;

    private Integer orderIndex = 0;

    private String codingTemplate;

    private String expectedOutput;

    private String sampleInput;

    private String sampleOutput;

    private List<OptionRequest> options;
}
