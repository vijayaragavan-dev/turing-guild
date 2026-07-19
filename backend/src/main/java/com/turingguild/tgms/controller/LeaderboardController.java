package com.turingguild.tgms.controller;

import com.turingguild.tgms.dto.response.LeaderboardResponse;
import com.turingguild.tgms.dto.response.MessageResponse;
import com.turingguild.tgms.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping("/leaderboard/{eventId}")
    public ResponseEntity<List<LeaderboardResponse>> getLeaderboard(@PathVariable Long eventId) {
        return ResponseEntity.ok(leaderboardService.getLeaderboard(eventId));
    }

    @GetMapping("/admin/leaderboard/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LeaderboardResponse>> getLeaderboardAdmin(@PathVariable Long eventId) {
        return ResponseEntity.ok(leaderboardService.getLeaderboard(eventId));
    }

    @PostMapping("/admin/leaderboard/{eventId}/recompute")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> recomputeLeaderboard(@PathVariable Long eventId) {
        leaderboardService.recomputeLeaderboard(eventId);
        return ResponseEntity.ok(new MessageResponse("Leaderboard recomputed successfully"));
    }
}
