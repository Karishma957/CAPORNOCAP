package com.capornocap.dto;

import java.time.LocalDateTime;
import java.util.List;

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
public class QuizSubmitRequest {
    private Long playerId;
    private Genre genre;
    private Difficulty difficulty;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<QuizAnswer> answers;
}
