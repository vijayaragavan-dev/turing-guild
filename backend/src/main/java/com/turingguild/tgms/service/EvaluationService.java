package com.turingguild.tgms.service;

import com.turingguild.tgms.dto.request.EvaluateAnswerRequest;
import com.turingguild.tgms.dto.request.EvaluateSubmissionRequest;
import com.turingguild.tgms.dto.response.SubmissionResponse;
import com.turingguild.tgms.entity.Answer;
import com.turingguild.tgms.entity.Submission;
import com.turingguild.tgms.entity.enums.SubmissionStatus;
import com.turingguild.tgms.exception.BadRequestException;
import com.turingguild.tgms.exception.ResourceNotFoundException;
import com.turingguild.tgms.repository.AnswerRepository;
import com.turingguild.tgms.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final SubmissionRepository submissionRepository;
    private final AnswerRepository answerRepository;
    private final LeaderboardService leaderboardService;

    @Transactional
    public SubmissionResponse evaluateSubmission(Long submissionId, EvaluateSubmissionRequest request) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found"));

        if (submission.getStatus() == SubmissionStatus.EVALUATED) {
            throw new BadRequestException("Submission already evaluated");
        }

        for (EvaluateAnswerRequest evalReq : request.getAnswers()) {
            Answer answer = answerRepository.findById(evalReq.getAnswerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Answer not found: " + evalReq.getAnswerId()));

            if (!answer.getSubmission().getId().equals(submissionId)) {
                throw new BadRequestException("Answer does not belong to this submission");
            }

            answer.setScoreAwarded(evalReq.getScoreAwarded());
            answer.setFeedback(evalReq.getFeedback());
            answer.setIsCorrect(evalReq.getScoreAwarded() > 0);
            answerRepository.save(answer);
        }

        submission.setStatus(SubmissionStatus.EVALUATED);
        submission.setTotalScore(answerRepository.findBySubmissionId(submissionId).stream()
                .mapToInt(Answer::getScoreAwarded)
                .sum());
        submissionRepository.save(submission);

        leaderboardService.updateLeaderboard(submission);

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
}
