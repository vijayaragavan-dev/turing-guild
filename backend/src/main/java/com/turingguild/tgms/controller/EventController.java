package com.turingguild.tgms.controller;

import com.turingguild.tgms.dto.request.CreateEventRequest;
import com.turingguild.tgms.dto.request.CreateQuestionRequest;
import com.turingguild.tgms.dto.request.UpdateEventRequest;
import com.turingguild.tgms.dto.response.EventResponse;
import com.turingguild.tgms.dto.response.MessageResponse;
import com.turingguild.tgms.dto.response.QuestionResponse;
import com.turingguild.tgms.entity.enums.EventCategory;
import com.turingguild.tgms.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/events")
    public ResponseEntity<List<EventResponse>> getPublishedEvents() {
        return ResponseEntity.ok(eventService.getPublishedEvents());
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @GetMapping("/events/category/{category}")
    public ResponseEntity<List<EventResponse>> getEventsByCategory(@PathVariable EventCategory category) {
        return ResponseEntity.ok(eventService.getEventsByCategory(category));
    }

    @GetMapping("/admin/events")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @PostMapping("/admin/events")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> createEvent(
            @Valid @RequestBody CreateEventRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(eventService.createEvent(request, authentication.getName()));
    }

    @PutMapping("/admin/events/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEventRequest request) {
        return ResponseEntity.ok(eventService.updateEvent(id, request));
    }

    @PutMapping("/admin/events/{id}/publish")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> publishEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.publishEvent(id));
    }

    @PutMapping("/admin/events/{id}/close")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventResponse> closeEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.closeEvent(id));
    }

    @DeleteMapping("/admin/events/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok(new MessageResponse("Event deleted successfully"));
    }

    @GetMapping("/admin/events/{eventId}/questions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<QuestionResponse>> getEventQuestions(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEventQuestions(eventId));
    }

    @PostMapping("/admin/events/{eventId}/questions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<QuestionResponse> addQuestion(
            @PathVariable Long eventId,
            @Valid @RequestBody CreateQuestionRequest request) {
        return ResponseEntity.ok(eventService.addQuestion(eventId, request));
    }

    @DeleteMapping("/admin/questions/{questionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteQuestion(@PathVariable Long questionId) {
        eventService.deleteQuestion(questionId);
        return ResponseEntity.ok(new MessageResponse("Question deleted successfully"));
    }
}
