package com.turingguild.tgms.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OptionRequest {

    @NotBlank(message = "Option text is required")
    private String optionText;

    private Boolean isCorrect = false;

    private Integer orderIndex = 0;
}
