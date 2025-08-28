package com.capornocap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIRecommendationRequestEvent {
    private Long playerId;
    private Integer limit;
}
