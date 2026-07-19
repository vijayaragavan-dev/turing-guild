package com.turingguild.tgms.handler;

import com.turingguild.tgms.entity.Answer;
import com.turingguild.tgms.entity.Question;
import com.turingguild.tgms.entity.enums.QuestionType;
import org.springframework.stereotype.Component;

@Component
public class MatchHandler implements QuestionHandler {

    @Override
    public void evaluate(Answer answer, Question question) {
        answer.setIsCorrect(null);
        answer.setScoreAwarded(0);
        answer.setFeedback("Requires manual evaluation");
    }

    @Override
    public boolean canHandle(Question question) {
        return question.getQuestionType() == QuestionType.MATCH;
    }
}
