package com.capornocap.kafka;

public class KafkaTopics {
    public static final String PLAYER_ACTIVITY = "player-activity-event";
    public static final String SCORE = "score-event";
}
/*
 * Kafka Topic | What It Does | Who Triggers It | Who Listens / Consumes It
 * user-activity-events | Sends info when a player starts or finishes a quiz |
 * Backend (PlayerService / QuizService) | AnalyticsService, AIService
 * score-events | Publishes the final score after quiz submission | ScoreService
 * (Backend) | AnalyticsService, LeaderboardService, AIService
 * ai-recommendation-requests | Triggers a recommendation for a user |
 * RecommendationService | AIService
 * ai-recommendation-responses| Returns recommended genres/difficulties for a
 * user | AIService → KafkaProducer | Frontend via RecommendationService
 */