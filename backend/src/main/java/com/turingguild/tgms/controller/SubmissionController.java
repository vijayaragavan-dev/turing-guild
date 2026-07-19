package com.turingguild.tgms.controller;

import com.turingguild.tgms.dto.request.SubmitAnswersRequest;
import com.turingguild.tgms.dto.response.SubmissionResponse;
import com.turingguild.tgms.service.SubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping("/events/{eventId}/join")
    public ResponseEntity<SubmissionResponse> joinEvent(
            @PathVariable Long eventId,
            Authentication authentication) {
        return ResponseEntity.ok(submissionService.joinEvent(eventId, authentication.getName()));
    }

    @GetMapping("/submissions/{id}")
    public ResponseEntity<SubmissionResponse> getSubmission(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(submissionService.getSubmission(id, authentication.getName()));
    }

    @PostMapping("/submissions/{id}/submit")
    public ResponseEntity<SubmissionResponse> submitAnswers(
            @PathVariable Long id,
            @Valid @RequestBody SubmitAnswersRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(submissionService.submitAnswers(id, request, authentication.getName()));
    }

    @GetMapping("/results/me")
    public ResponseEntity<?> getMyResults(Authentication authentication) {
        return ResponseEntity.ok(submissionService.getSubmissionsByUsername(authentication.getName()));
    }
}
