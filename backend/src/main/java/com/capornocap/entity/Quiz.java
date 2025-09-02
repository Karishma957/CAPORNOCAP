package com.capornocap.entity;

import java.time.Instant;
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
    private Instant startTime;

    @Column(nullable = false)
    private Instant endTime;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "quiz_id")
    private List<Answer> answers;

    private Integer score;
}
