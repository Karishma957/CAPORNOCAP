package com.capornocap.model;

import java.util.List;

import com.capornocap.dto.AchievementDTO;
import com.capornocap.entity.Answer;

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
