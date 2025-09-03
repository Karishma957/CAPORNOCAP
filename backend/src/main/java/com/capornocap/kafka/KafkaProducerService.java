package com.capornocap.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.capornocap.model.PlayerActivityEvent;
import com.capornocap.model.ScoreEvent;

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
}
