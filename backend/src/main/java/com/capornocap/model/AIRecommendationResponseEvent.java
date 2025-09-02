package com.capornocap.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIRecommendationResponseEvent {
    private Long playerId;
    private List<RecommendationItem> recommendations;
    private LocalDateTime generatedAt;
}
