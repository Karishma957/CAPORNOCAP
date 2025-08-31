package com.capornocap.dto;

import com.capornocap.utils.Achievement;

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
    private Achievement achievement;
}
