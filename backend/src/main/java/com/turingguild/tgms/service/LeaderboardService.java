package com.turingguild.tgms.service;

import com.turingguild.tgms.dto.response.LeaderboardResponse;
import com.turingguild.tgms.entity.Leaderboard;
import com.turingguild.tgms.entity.Submission;
import com.turingguild.tgms.entity.User;
import com.turingguild.tgms.entity.enums.SubmissionStatus;
import com.turingguild.tgms.exception.ResourceNotFoundException;
import com.turingguild.tgms.repository.LeaderboardRepository;
import com.turingguild.tgms.repository.SubmissionRepository;
import com.turingguild.tgms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final LeaderboardRepository leaderboardRepository;
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;

    public List<LeaderboardResponse> getLeaderboard(Long eventId) {
        return leaderboardRepository.findByEventIdWithUser(eventId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void updateLeaderboard(Submission submission) {
        Leaderboard existing = leaderboardRepository
                .findByUserIdAndEventId(submission.getUser().getId(), submission.getEvent().getId())
                .orElse(null);

        if (existing == null) {
            existing = Leaderboard.builder()
                    .user(submission.getUser())
                    .event(submission.getEvent())
                    .totalScore(submission.getTotalScore())
                    .build();
        } else {
            existing.setTotalScore(submission.getTotalScore());
        }

        leaderboardRepository.save(existing);
        recomputeRanks(submission.getEvent().getId());
    }

    @Transactional
    public void recomputeLeaderboard(Long eventId) {
        leaderboardRepository.deleteByEventId(eventId);

        List<Submission> evaluatedSubmissions = submissionRepository.findByEventIdWithDetails(eventId)
                .stream()
                .filter(s -> s.getStatus() == SubmissionStatus.EVALUATED)
                .toList();

        for (Submission submission : evaluatedSubmissions) {
            Leaderboard entry = Leaderboard.builder()
                    .user(submission.getUser())
                    .event(submission.getEvent())
                    .totalScore(submission.getTotalScore())
                    .build();
            leaderboardRepository.save(entry);
        }

        recomputeRanks(eventId);
    }

    private void recomputeRanks(Long eventId) {
        List<Leaderboard> entries = leaderboardRepository.findByEventIdWithUser(eventId);
        int rank = 1;
        for (Leaderboard entry : entries) {
            entry.setRank(rank++);
            leaderboardRepository.save(entry);
        }
    }

    private LeaderboardResponse toResponse(Leaderboard leaderboard) {
        return LeaderboardResponse.builder()
                .rank(leaderboard.getRank())
                .userId(leaderboard.getUser().getId())
                .userBatchNumber(leaderboard.getUser().getBatchNumber())
                .userFullName(leaderboard.getUser().getFullName())
                .eventId(leaderboard.getEvent().getId())
                .eventTitle(leaderboard.getEvent().getTitle())
                .totalScore(leaderboard.getTotalScore())
                .build();
    }
}
