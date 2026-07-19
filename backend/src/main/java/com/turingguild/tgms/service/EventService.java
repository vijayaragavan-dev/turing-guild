package com.turingguild.tgms.service;

import com.turingguild.tgms.dto.request.CreateEventRequest;
import com.turingguild.tgms.dto.request.CreateQuestionRequest;
import com.turingguild.tgms.dto.request.OptionRequest;
import com.turingguild.tgms.dto.request.UpdateEventRequest;
import com.turingguild.tgms.dto.response.EventResponse;
import com.turingguild.tgms.dto.response.OptionResponse;
import com.turingguild.tgms.dto.response.QuestionResponse;
import com.turingguild.tgms.entity.*;
import com.turingguild.tgms.entity.enums.EventCategory;
import com.turingguild.tgms.entity.enums.EventStatus;
import com.turingguild.tgms.exception.BadRequestException;
import com.turingguild.tgms.exception.ResourceNotFoundException;
import com.turingguild.tgms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final UserRepository userRepository;

    public List<EventResponse> getAllEvents() {
        return eventRepository.findByStatusNot(EventStatus.DELETED).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<EventResponse> getPublishedEvents() {
        return eventRepository.findAvailableEvents().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<EventResponse> getEventsByCategory(EventCategory category) {
        return eventRepository.findByStatusAndCategory(EventStatus.PUBLISHED, category).stream()
                .map(this::toResponse)
                .toList();
    }

    public EventResponse getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .filter(e -> e.getStatus() != EventStatus.DELETED)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        return toResponseWithQuestions(event);
    }

    @Transactional
    public EventResponse createEvent(CreateEventRequest request, String username) {
        User creator = userRepository.findByBatchNumberOrEmail(username, username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Event event = Event.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .status(EventStatus.DRAFT)
                .durationMinutes(request.getDurationMinutes())
                .totalMarks(0)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .createdBy(creator)
                .build();

        return toResponse(eventRepository.save(event));
    }

    @Transactional
    public EventResponse updateEvent(Long id, UpdateEventRequest request) {
        Event event = eventRepository.findById(id)
                .filter(e -> e.getStatus() == EventStatus.DRAFT)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found or not editable"));

        if (request.getTitle() != null) event.setTitle(request.getTitle());
        if (request.getDescription() != null) event.setDescription(request.getDescription());
        if (request.getDurationMinutes() != null) event.setDurationMinutes(request.getDurationMinutes());

        return toResponse(eventRepository.save(event));
    }

    @Transactional
    public EventResponse publishEvent(Long id) {
        Event event = eventRepository.findById(id)
                .filter(e -> e.getStatus() == EventStatus.DRAFT)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found or not in draft status"));

        if (event.getDurationMinutes() == null || event.getDurationMinutes() <= 0) {
            throw new BadRequestException("Event must have a valid duration");
        }

        long questionCount = questionRepository.countByEventId(id);
        if (questionCount == 0) {
            throw new BadRequestException("Event must have at least one question to publish");
        }

        event.setStatus(EventStatus.PUBLISHED);
        return toResponse(eventRepository.save(event));
    }

    @Transactional
    public EventResponse closeEvent(Long id) {
        Event event = eventRepository.findById(id)
                .filter(e -> e.getStatus() == EventStatus.PUBLISHED)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found or not published"));

        event.setStatus(EventStatus.CLOSED);
        return toResponse(eventRepository.save(event));
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        event.setStatus(EventStatus.DELETED);
        eventRepository.save(event);
    }

    @Transactional
    public QuestionResponse addQuestion(Long eventId, CreateQuestionRequest request) {
        Event event = eventRepository.findById(eventId)
                .filter(e -> e.getStatus() == EventStatus.DRAFT)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found or not editable"));

        Question question = Question.builder()
                .event(event)
                .questionType(request.getQuestionType())
                .questionText(request.getQuestionText())
                .marks(request.getMarks() != null ? request.getMarks() : 1)
                .orderIndex(request.getOrderIndex() != null ? request.getOrderIndex() : 0)
                .codingTemplate(request.getCodingTemplate())
                .expectedOutput(request.getExpectedOutput())
                .sampleInput(request.getSampleInput())
                .sampleOutput(request.getSampleOutput())
                .build();

        question = questionRepository.save(question);

        if (request.getOptions() != null && !request.getOptions().isEmpty()) {
            for (OptionRequest optReq : request.getOptions()) {
                Option option = Option.builder()
                        .question(question)
                        .optionText(optReq.getOptionText())
                        .isCorrect(optReq.getIsCorrect() != null ? optReq.getIsCorrect() : false)
                        .orderIndex(optReq.getOrderIndex() != null ? optReq.getOrderIndex() : 0)
                        .build();
                question.getOptions().add(option);
            }
            question = questionRepository.save(question);
        }

        event.setTotalMarks(event.getTotalMarks() + question.getMarks());
        eventRepository.save(event);

        return toQuestionResponse(question);
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        Event event = question.getEvent();
        event.setTotalMarks(event.getTotalMarks() - question.getMarks());
        eventRepository.save(event);

        questionRepository.delete(question);
    }

    public List<QuestionResponse> getEventQuestions(Long eventId) {
        return questionRepository.findByEventIdWithOptions(eventId).stream()
                .map(this::toQuestionResponse)
                .toList();
    }

    private EventResponse toResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .category(event.getCategory())
                .status(event.getStatus())
                .durationMinutes(event.getDurationMinutes())
                .totalMarks(event.getTotalMarks())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .createdBy(event.getCreatedBy().getFullName())
                .createdAt(event.getCreatedAt())
                .build();
    }

    private EventResponse toResponseWithQuestions(Event event) {
        EventResponse response = toResponse(event);
        List<QuestionResponse> questions = questionRepository.findByEventIdWithOptions(event.getId()).stream()
                .map(this::toQuestionResponse)
                .toList();
        response.setQuestions(questions);
        return response;
    }

    private QuestionResponse toQuestionResponse(Question question) {
        List<OptionResponse> options = question.getOptions().stream()
                .map(opt -> OptionResponse.builder()
                        .id(opt.getId())
                        .optionText(opt.getOptionText())
                        .orderIndex(opt.getOrderIndex())
                        .build())
                .toList();

        return QuestionResponse.builder()
                .id(question.getId())
                .questionType(question.getQuestionType())
                .questionText(question.getQuestionText())
                .marks(question.getMarks())
                .orderIndex(question.getOrderIndex())
                .codingTemplate(question.getCodingTemplate())
                .expectedOutput(question.getExpectedOutput())
                .sampleInput(question.getSampleInput())
                .sampleOutput(question.getSampleOutput())
                .options(options)
                .build();
    }
}
