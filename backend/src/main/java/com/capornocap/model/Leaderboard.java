package com.capornocap.model;

import lombok.Data;

@Data
public class Leaderboard {
    private String username;
    private Integer xp;
    private String avatarUrl;

    public Leaderboard(String username, Integer xp, String avatarUrl) {
        this.username = username;
        this.xp = xp;
        this.avatarUrl = avatarUrl;
    }
}