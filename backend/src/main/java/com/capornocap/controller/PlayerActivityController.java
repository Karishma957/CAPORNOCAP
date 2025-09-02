package com.capornocap.controller;

import com.capornocap.kafka.KafkaProducerService;
import com.capornocap.kafka.event.PlayerActivityEvent;
import com.capornocap.utils.UserActivityType;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlayerActivityController {

    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/player-activity")
    public String testPlayerActivity() {
        PlayerActivityEvent event = PlayerActivityEvent.builder()
                .playerId(1L)
                .quizSessionId(100L)
                .userActivityType(UserActivityType.QUIZ_STARTED)
                .fieldName("status")
                .beforeValue("inactive")
                .afterValue("active")
                .build();
        kafkaProducerService.sendPlayerActivityEvent(event);
        return "PlayerActivityEvent sent.";
    }

}
