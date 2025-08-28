package com.capornocap.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScoreEvent {
    private Long playerId;
    private Long quizSessionId;
    private int score;
    private int totalQuestions;
    private int correctAnswers;
    private String difficulty;
    private Long genreId;
    private String genreName;
}