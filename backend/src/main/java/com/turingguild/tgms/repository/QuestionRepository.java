package com.turingguild.tgms.repository;

import com.turingguild.tgms.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByEventId(Long eventId);
    List<Question> findByEventIdOrderByOrderIndex(Long eventId);
    
    @Query("SELECT q FROM Question q LEFT JOIN FETCH q.options WHERE q.event.id = :eventId ORDER BY q.orderIndex")
    List<Question> findByEventIdWithOptions(Long eventId);
    
    long countByEventId(Long eventId);
}
