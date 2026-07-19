package com.turingguild.tgms.controller;

import com.turingguild.tgms.dto.request.EvaluateSubmissionRequest;
import com.turingguild.tgms.dto.response.SubmissionResponse;
import com.turingguild.tgms.service.EvaluationService;
import com.turingguild.tgms.service.SubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/submissions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminSubmissionController {

    private final SubmissionService submissionService;
    private final EvaluationService evaluationService;

    @GetMapping
    public ResponseEntity<List<SubmissionResponse>> getSubmissionsByEvent(@RequestParam Long eventId) {
        return ResponseEntity.ok(submissionService.getSubmissionsByEvent(eventId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubmissionResponse> getSubmission(@PathVariable Long id) {
        return ResponseEntity.ok(submissionService.getSubmissionById(id));
    }

    @PutMapping("/{id}/evaluate")
    public ResponseEntity<SubmissionResponse> evaluateSubmission(
            @PathVariable Long id,
            @Valid @RequestBody EvaluateSubmissionRequest request) {
        return ResponseEntity.ok(evaluationService.evaluateSubmission(id, request));
    }
}
