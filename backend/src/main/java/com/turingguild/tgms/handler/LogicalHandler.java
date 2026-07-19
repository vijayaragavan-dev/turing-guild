package com.turingguild.tgms.handler;

import com.turingguild.tgms.entity.Answer;
import com.turingguild.tgms.entity.Question;
import com.turingguild.tgms.entity.enums.QuestionType;
import com.turingguild.tgms.repository.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LogicalHandler implements QuestionHandler {

    private final OptionRepository optionRepository;

    @Override
    public void evaluate(Answer answer, Question question) {
        List<String> correctOptions = optionRepository.findByQuestionIdOrderByOrderIndex(question.getId())
                .stream()
                .filter(opt -> opt.getIsCorrect())
                .map(opt -> opt.getOptionText().toLowerCase().trim())
                .toList();

        String userAnswer = answer.getAnswerText() != null ? answer.getAnswerText().toLowerCase().trim() : "";
        boolean isCorrect = correctOptions.contains(userAnswer);

        answer.setIsCorrect(isCorrect);
        answer.setScoreAwarded(isCorrect ? question.getMarks() : 0);
    }

    @Override
    public boolean canHandle(Question question) {
        return question.getQuestionType() == QuestionType.LOGICAL;
    }
}
