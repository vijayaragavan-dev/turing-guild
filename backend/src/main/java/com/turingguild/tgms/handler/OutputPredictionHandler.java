package com.turingguild.tgms.handler;

import com.turingguild.tgms.entity.Answer;
import com.turingguild.tgms.entity.Question;
import com.turingguild.tgms.entity.enums.QuestionType;
import org.springframework.stereotype.Component;

@Component
public class OutputPredictionHandler implements QuestionHandler {

    @Override
    public void evaluate(Answer answer, Question question) {
        if (question.getExpectedOutput() != null) {
            String expected = question.getExpectedOutput().trim();
            String userAnswer = answer.getAnswerText() != null ? answer.getAnswerText().trim() : "";
            boolean isCorrect = expected.equals(userAnswer);
            answer.setIsCorrect(isCorrect);
            answer.setScoreAwarded(isCorrect ? question.getMarks() : 0);
        }
    }

    @Override
    public boolean canHandle(Question question) {
        return question.getQuestionType() == QuestionType.OUTPUT_PREDICTION;
    }
}
