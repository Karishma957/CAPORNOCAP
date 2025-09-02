package com.capornocap.dto;

import java.util.List;

import com.capornocap.model.Leaderboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaderboardDto {
    private List<Leaderboard> players;
    private long totalCount;
    private int totalPages;
    private int currentPage;
}
