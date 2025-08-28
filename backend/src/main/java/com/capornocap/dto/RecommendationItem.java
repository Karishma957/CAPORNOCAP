package com.capornocap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationItem {
    private String genreName;
    private String difficulty;
    private double confidenceScore;
}
