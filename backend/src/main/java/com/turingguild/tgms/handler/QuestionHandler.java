package com.turingguild.tgms.handler;

import com.turingguild.tgms.entity.Answer;
import com.turingguild.tgms.entity.Question;

public interface QuestionHandler {
    void evaluate(Answer answer, Question question);
    boolean canHandle(Question question);
}
