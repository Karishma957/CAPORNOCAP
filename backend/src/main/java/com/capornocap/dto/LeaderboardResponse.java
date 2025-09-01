package com.capornocap.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaderboardResponse {
    private List<Leaderboard> players;
    private long totalCount;
    private int totalPages;
    private int currentPage;
}
