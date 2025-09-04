package com.capornocap.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.capornocap.model.PlayerActivityEvent;
import com.capornocap.model.ScoreEvent;
import com.capornocap.service.DeveloperConsoleService;
import com.capornocap.utils.UserActivityType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaAnalyticsConsumerService {

    private final DeveloperConsoleService developerConsoleService;

    @KafkaListener(topics = KafkaTopics.PLAYER_ACTIVITY, groupId = KafkaConsumerGroups.ANALYTICS_CONSUMER_GROUP_ID)
    public void analyticsGroupHandlePlayerActivity(PlayerActivityEvent message) {
        if (message.getUserActivityType() == null) {
            System.out.println(
                    "Analytics Group has received PLAYER_ACTIVITY_EVENT message with null activity type: " + message);
            return;
        }
        UserActivityType activityType = message.getUserActivityType();
        switch (activityType) {
            case LOGIN:
                developerConsoleService.onLogin(message.getPlayerId());
                break;
            case LOGOUT:
                developerConsoleService.onLogout(message.getPlayerId());
                break;
            case QUIZ_STARTED:
                developerConsoleService.onQuizStarted(message.getPlayerId());
                break;
            case QUIZ_ENDED:
                developerConsoleService.onQuizEnded(message.getPlayerId());
                break;
            case RECOMMENDATION_ACCEPTED:
                developerConsoleService.onRecommendationAccepted();
                break;
            default:
                break;
        }
    }

    @KafkaListener(topics = KafkaTopics.SCORE, groupId = KafkaConsumerGroups.ANALYTICS_CONSUMER_GROUP_ID)
    public void analyticsGroupHandleScore(ScoreEvent message) {
        if (message.getQuizSessionId() == null) {
            System.out.println("Analytics Group has received SCORE_EVENT message with null quizSessionId: " + message);
            return;
        }
        developerConsoleService.onScore(message.getGenreName(), message.getDifficulty(), message.getScore());
    }

}
