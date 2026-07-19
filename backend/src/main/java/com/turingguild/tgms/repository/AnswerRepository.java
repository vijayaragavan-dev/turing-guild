package com.turingguild.tgms.repository;

import com.turingguild.tgms.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findBySubmissionId(Long submissionId);
    Optional<Answer> findBySubmissionIdAndQuestionId(Long submissionId, Long questionId);
    boolean existsBySubmissionIdAndQuestionId(Long submissionId, Long questionId);
}
