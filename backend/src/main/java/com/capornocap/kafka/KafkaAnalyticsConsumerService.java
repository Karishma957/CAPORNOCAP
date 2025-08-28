package com.capornocap.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.capornocap.kafka.event.PlayerActivityEvent;
import com.capornocap.kafka.event.ScoreEvent;

@Service
public class KafkaAnalyticsConsumerService {

    @KafkaListener(topics = KafkaTopics.PLAYER_ACTIVITY, groupId = KafkaConsumerGroups.ANALYTICS_CONSUMER_GROUP_ID)
    public void analyticsGroupHandlePlayerActivity(PlayerActivityEvent message) {
        System.out.println("Analytics Group has reciever PLAYER_ACTIVITY_EVENT message: " + message);
    }

    @KafkaListener(topics = KafkaTopics.SCORE, groupId = KafkaConsumerGroups.ANALYTICS_CONSUMER_GROUP_ID)
    public void analyticsGroupHandleScore(ScoreEvent message) {
        System.out.println("Analytics Group has reciever SCORE_EVENT message: " + message);
    }

}
