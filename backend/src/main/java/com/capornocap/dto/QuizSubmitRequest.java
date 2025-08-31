package com.capornocap.dto;

import java.time.Instant;
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
    private Instant startTime;
    private Instant endTime;
    private List<QuizAnswer> answers;
}
