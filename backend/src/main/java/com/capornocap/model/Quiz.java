package com.capornocap.model;

import java.time.LocalDateTime;
import java.util.List;

import com.capornocap.utils.Difficulty;
import com.capornocap.utils.Genre;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long playerId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Genre genre;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Difficulty difficulty;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @ElementCollection
    @CollectionTable(name = "quiz_answers", joinColumns = @JoinColumn(name = "quiz_attempt_id"))
    private List<Answer> answers;

    private Integer score;
}
