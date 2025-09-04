package com.capornocap.dto;

import java.util.List;

import com.capornocap.model.MostPlayed;
import com.capornocap.model.ScoresCombo;
import com.capornocap.model.TimeSeriesByMinute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperConsoleState {

    private Long onlinePlayers;
    private Long activeQuizzes;

    private Long totalPlayers;
    private Long totalQuizzesPlayed;
    private Long totalRecommendationsAccepted;

    private List<TimeSeriesByMinute> timeSeriesByMinute;
    private List<MostPlayed> mostPlayed;
    private List<ScoresCombo> scoresByCombo;
}
