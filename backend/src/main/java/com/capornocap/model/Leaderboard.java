package com.capornocap.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Leaderboard {
    private String username;
    private Integer xp;
    private String avatarUrl;
}