package com.turingguild.tgms.repository;

import com.turingguild.tgms.entity.Event;
import com.turingguild.tgms.entity.enums.EventCategory;
import com.turingguild.tgms.entity.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByStatus(EventStatus status);
    List<Event> findByStatusNot(EventStatus status);
    List<Event> findByStatusAndCategory(EventStatus status, EventCategory category);
    List<Event> findByCreatedBy_Id(Long userId);
    
    @Query("SELECT e FROM Event e WHERE e.status = 'PUBLISHED' AND (e.startTime IS NULL OR e.startTime <= CURRENT_TIMESTAMP)")
    List<Event> findAvailableEvents();
}
