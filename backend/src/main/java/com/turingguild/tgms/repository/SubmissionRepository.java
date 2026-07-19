package com.turingguild.tgms.repository;

import com.turingguild.tgms.entity.Submission;
import com.turingguild.tgms.entity.enums.SubmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    Optional<Submission> findByUserIdAndEventId(Long userId, Long eventId);
    List<Submission> findByEventId(Long eventId);
    List<Submission> findByEventIdAndStatus(Long eventId, SubmissionStatus status);
    List<Submission> findByUserId(Long userId);
    boolean existsByUserIdAndEventId(Long userId, Long eventId);
    
    @Query("SELECT s FROM Submission s JOIN FETCH s.user u JOIN FETCH s.event e WHERE s.event.id = :eventId ORDER BY s.totalScore DESC")
    List<Submission> findByEventIdWithDetails(Long eventId);
}
