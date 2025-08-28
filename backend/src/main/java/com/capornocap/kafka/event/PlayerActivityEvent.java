package com.capornocap.kafka.event;

import com.capornocap.utils.UserActivityType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerActivityEvent {
    private Long playerId;
    private Long quizSessionId;
    private UserActivityType userActivityType;
    private String fieldName;
    private String beforeValue;
    private String afterValue;
}
