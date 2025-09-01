package com.capornocap.dto;

import com.capornocap.utils.Difficulty;
import com.capornocap.utils.Genre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationItem {
    private Genre genreName;
    private Difficulty difficulty;
    private double confidenceScore;
}
