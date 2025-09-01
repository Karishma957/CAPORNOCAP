package com.capornocap.kafka.event;

import com.capornocap.utils.Difficulty;
import com.capornocap.utils.Genre;

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
    private Difficulty difficulty;
    private Genre genreName;
}