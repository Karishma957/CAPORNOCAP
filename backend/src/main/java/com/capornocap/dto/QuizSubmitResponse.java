package com.capornocap.dto;

import java.util.List;

import com.capornocap.model.Answer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmitResponse {
    private Integer score;
    private Integer totalQuestions;
    private AchievementDTO achievement;
    private List<Answer> answers;
}
