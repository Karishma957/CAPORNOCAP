package com.capornocap.dto;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerDTO {
    private Long id;
    private String username;
    private Integer xp;
    private String avatarUrl;
    private Instant createdAt;
    private AchievementDTO achievement;
}
