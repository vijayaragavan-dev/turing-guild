package com.turingguild.tgms.service;

import com.turingguild.tgms.dto.request.AnswerRequest;
import com.turingguild.tgms.dto.request.SubmitAnswersRequest;
import com.turingguild.tgms.dto.response.AnswerResponse;
import com.turingguild.tgms.dto.response.SubmissionResponse;
import com.turingguild.tgms.entity.*;
import com.turingguild.tgms.entity.enums.EventStatus;
import com.turingguild.tgms.entity.enums.SubmissionStatus;
import com.turingguild.tgms.exception.BadRequestException;
import com.turingguild.tgms.exception.ResourceNotFoundException;
import com.turingguild.tgms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AnswerRepository answerRepository;
    private final EventRepository eventRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final UserRepository userRepository;

    @Transactional
    public SubmissionResponse joinEvent(Long eventId, String username) {
        User user = userRepository.findByBatchNumberOrEmail(username, username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Event event = eventRepository.findById(eventId)
                .filter(e -> e.getStatus() == EventStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found or not published"));

        if (submissionRepository.existsByUserIdAndEventId(user.getId(), eventId)) {
            throw new BadRequestException("Already joined this event");
        }

        Submission submission = Submission.builder()
                .user(user)
                .event(event)
                .status(SubmissionStatus.IN_PROGRESS)
                .totalScore(0)
                .build();

        return toResponse(submissionRepository.save(submission));
    }

    public SubmissionResponse getSubmission(Long submissionId, String username) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found"));

        User user = userRepository.findByBatchNumberOrEmail(username, username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!submission.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Access denied");
        }

        return toResponseWithAnswers(submission);
    }

    public SubmissionResponse getSubmissionById(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found"));
        return toResponseWithAnswers(submission);
    }

    public List<SubmissionResponse> getSubmissionsByEvent(Long eventId) {
        return submissionRepository.findByEventIdWithDetails(eventId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<SubmissionResponse> getSubmissionsByUsername(String username) {
        User user = userRepository.findByBatchNumberOrEmail(username, username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return submissionRepository.findByUserId(user.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SubmissionResponse submitAnswers(Long submissionId, SubmitAnswersRequest request, String username) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found"));

        User user = userRepository.findByBatchNumberOrEmail(username, username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!submission.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Access denied");
        }

        if (submission.getStatus() == SubmissionStatus.SUBMITTED ||
            submission.getStatus() == SubmissionStatus.EVALUATED) {
            throw new BadRequestException("Submission already submitted");
        }

        Event event = submission.getEvent();
        if (event.getEndTime() != null && LocalDateTime.now().isAfter(event.getEndTime())) {
            throw new BadRequestException("Event has ended");
        }

        Map<Long, Question> questionMap = questionRepository.findByEventId(event.getId()).stream()
                .collect(Collectors.toMap(Question::getId, q -> q));

        for (AnswerRequest answerReq : request.getAnswers()) {
            Question question = questionMap.get(answerReq.getQuestionId());
            if (question == null) {
                throw new BadRequestException("Invalid question ID: " + answerReq.getQuestionId());
            }

            Answer answer = Answer.builder()
                    .submission(submission)
                    .question(question)
                    .answerText(answerReq.getAnswerText())
                    .build();

            autoEvaluate(answer, question);
            submission.getAnswers().add(answer);
        }

        submission.setStatus(SubmissionStatus.SUBMITTED);
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setTotalScore(submission.getAnswers().stream()
                .mapToInt(Answer::getScoreAwarded)
                .sum());

        return toResponse(submissionRepository.save(submission));
    }

    private void autoEvaluate(Answer answer, Question question) {
        switch (question.getQuestionType()) {
            case MCQ, LOGICAL, NUMERICAL -> {
                List<Option> correctOptions = optionRepository.findByQuestionIdOrderByOrderIndex(question.getId())
                        .stream()
                        .filter(Option::getIsCorrect)
                        .toList();
                
                if (!correctOptions.isEmpty()) {
                    boolean isCorrect = correctOptions.stream()
                            .anyMatch(opt -> opt.getOptionText().equalsIgnoreCase(answer.getAnswerText()));
                    answer.setIsCorrect(isCorrect);
                    answer.setScoreAwarded(isCorrect ? question.getMarks() : 0);
                }
            }
            case FILL_BLANKS -> {
                if (question.getExpectedOutput() != null) {
                    boolean isCorrect = question.getExpectedOutput().trim()
                            .equalsIgnoreCase(answer.getAnswerText() != null ? answer.getAnswerText().trim() : "");
                    answer.setIsCorrect(isCorrect);
                    answer.setScoreAwarded(isCorrect ? question.getMarks() : 0);
                }
            }
            default -> {
                answer.setIsCorrect(null);
                answer.setScoreAwarded(0);
            }
        }
    }

    private SubmissionResponse toResponse(Submission submission) {
        return SubmissionResponse.builder()
                .id(submission.getId())
                .userId(submission.getUser().getId())
                .userBatchNumber(submission.getUser().getBatchNumber())
                .eventId(submission.getEvent().getId())
                .eventTitle(submission.getEvent().getTitle())
                .status(submission.getStatus())
                .totalScore(submission.getTotalScore())
                .submittedAt(submission.getSubmittedAt())
                .createdAt(submission.getCreatedAt())
                .build();
    }

    private SubmissionResponse toResponseWithAnswers(Submission submission) {
        SubmissionResponse response = toResponse(submission);
        List<AnswerResponse> answers = answerRepository.findBySubmissionId(submission.getId()).stream()
                .map(ans -> AnswerResponse.builder()
                        .id(ans.getId())
                        .questionId(ans.getQuestion().getId())
                        .answerText(ans.getAnswerText())
                        .isCorrect(ans.getIsCorrect())
                        .scoreAwarded(ans.getScoreAwarded())
                        .feedback(ans.getFeedback())
                        .build())
                .toList();
        response.setAnswers(answers);
        return response;
    }
}
