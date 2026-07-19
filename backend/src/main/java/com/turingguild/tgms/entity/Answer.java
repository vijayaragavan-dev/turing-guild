package com.turingguild.tgms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "answers", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"submission_id", "question_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(columnDefinition = "TEXT")
    private String answerText;

    private Boolean isCorrect;

    @Column(nullable = false)
    @Builder.Default
    private Integer scoreAwarded = 0;

    @Column(columnDefinition = "TEXT")
    private String feedback;
}
