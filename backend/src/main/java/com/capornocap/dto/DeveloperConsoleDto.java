package com.capornocap.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeveloperConsoleDto {
    public long totalLoggedIn;
    public long totalLoggedOut;
    public long totalQuizStarted;
    public long totalQuizEnded;
    public long totalAchievementsUnlocked;
    public long totalRecommendationsAccepted;

    // genre-> difficulty -> count and average score
    public Map<String, Map<String, GenreDiffStats>> genreDifficultyStats = new HashMap<>();

    // optional: last N timestamps for active counts
    public List<TimePoint> recentActiveCounts = new ArrayList<>();
}
