package com.turingguild.tgms.repository;

import com.turingguild.tgms.entity.Leaderboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {
    List<Leaderboard> findByEventIdOrderByRankAsc(Long eventId);
    Optional<Leaderboard> findByUserIdAndEventId(Long userId, Long eventId);
    void deleteByEventId(Long eventId);
    
    @Query("SELECT l FROM Leaderboard l JOIN FETCH l.user u WHERE l.event.id = :eventId ORDER BY l.totalScore DESC")
    List<Leaderboard> findByEventIdWithUser(Long eventId);
}
