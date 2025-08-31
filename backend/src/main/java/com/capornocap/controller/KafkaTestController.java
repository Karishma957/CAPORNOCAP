package com.capornocap.controller;

import com.capornocap.kafka.KafkaProducerService;
import com.capornocap.kafka.event.PlayerActivityEvent;
import com.capornocap.kafka.event.ScoreEvent;
import com.capornocap.utils.UserActivityType;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kafka/test")
@RequiredArgsConstructor
public class KafkaTestController {

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

    @PostMapping("/score-event")
    public String testScoreEvent(@RequestParam Long playerId) {
        List<String> difficulties = List.of("EASY", "MEDIUM", "HARD");
        List<String> genres = List.of("ENTERTAINMENT", "SCIENCE", "MUSIC");

        // Seed quizSessionId based on playerId so each player has unique sessions
        long baseSessionId = playerId * 1000;

        for (int i = 0; i < difficulties.size(); i++) {
            ScoreEvent event = ScoreEvent.builder()
                    .playerId(playerId)
                    .quizSessionId(baseSessionId + i) // unique per player
                    .score(5 + (int) (Math.random() * 15)) // random score 5–20
                    .totalQuestions(5 + (i % 3) * 5) // 5, 10, 15 questions
                    .correctAnswers((int) (Math.random() * 5)) // 0–4 correct
                    .difficulty(difficulties.get(i))
                    .genreId((long) (i + 1))
                    .genreName(genres.get(i))
                    .build();

            kafkaProducerService.sendScoreEvent(event);
        }

        return "ScoreEvents sent for player " + playerId;
    }

}
