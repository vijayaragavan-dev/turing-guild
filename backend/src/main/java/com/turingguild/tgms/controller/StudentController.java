package com.turingguild.tgms.controller;

import com.turingguild.tgms.dto.request.CreateStudentRequest;
import com.turingguild.tgms.dto.request.UpdateStudentRequest;
import com.turingguild.tgms.dto.response.MessageResponse;
import com.turingguild.tgms.dto.response.StudentResponse;
import com.turingguild.tgms.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/students")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody CreateStudentRequest request) {
        return ResponseEntity.ok(studentService.createStudent(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStudentRequest request) {
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }

    @PostMapping("/{id}/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@PathVariable Long id) {
        studentService.resetPassword(id);
        return ResponseEntity.ok(new MessageResponse("Password reset successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(new MessageResponse("Student deactivated successfully"));
    }
}
