package com.turingguild.tgms.handler;

import com.turingguild.tgms.entity.Question;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionHandlerRegistry {

    private final List<QuestionHandler> handlers;

    public QuestionHandlerRegistry(List<QuestionHandler> handlers) {
        this.handlers = handlers;
    }

    public QuestionHandler getHandler(Question question) {
        return handlers.stream()
                .filter(h -> h.canHandle(question))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException(
                        "No handler for question type: " + question.getQuestionType()));
    }
}
