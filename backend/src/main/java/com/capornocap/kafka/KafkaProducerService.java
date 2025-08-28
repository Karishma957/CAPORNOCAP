package com.capornocap.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.capornocap.dto.AIRecommendationRequestEvent;
import com.capornocap.dto.AIRecommendationResponseEvent;
import com.capornocap.kafka.event.PlayerActivityEvent;
import com.capornocap.kafka.event.ScoreEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> KafkaTemplate;

    public void sendPlayerActivityEvent(PlayerActivityEvent event) {
        KafkaTemplate.send(KafkaTopics.PLAYER_ACTIVITY, event);
    }

    public void sendScoreEvent(ScoreEvent event) {
        KafkaTemplate.send(KafkaTopics.SCORE, event);
    }

    public void sendAiRecommendationRequestEvent(AIRecommendationRequestEvent event) {
        KafkaTemplate.send(KafkaTopics.AI_RECOMMENDATION_REQUEST, event);
    }

    public void sendAiRecommendationResponseEvent(AIRecommendationResponseEvent event) {
        KafkaTemplate.send(KafkaTopics.AI_RECOMMENDATION_RESPONSE, event);
    }
}
