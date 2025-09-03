package com.capornocap.model;

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
    private UserActivityType userActivityType;
    private String achievement;
}
