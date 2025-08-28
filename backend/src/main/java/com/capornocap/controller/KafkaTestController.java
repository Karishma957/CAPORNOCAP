package com.capornocap.controller;

import com.capornocap.dto.AIRecommendationRequestEvent;
import com.capornocap.dto.AIRecommendationResponseEvent;
import com.capornocap.dto.RecommendationItem;
import com.capornocap.kafka.KafkaProducerService;
import com.capornocap.kafka.event.PlayerActivityEvent;
import com.capornocap.kafka.event.ScoreEvent;
import com.capornocap.utils.UserActivityType;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
        List<String> genres = List.of("ART", "SCIENCE", "HISTORY");

        long quizSessionId = 100L;

        for (int i = 0; i < difficulties.size(); i++) {
            ScoreEvent event = ScoreEvent.builder()
                    .playerId(playerId)
                    .quizSessionId(quizSessionId + i) // different session IDs
                    .score(10 + i) // slightly varied scores
                    .totalQuestions(5)
                    .correctAnswers(3 + i % 2) // vary correct answers
                    .difficulty(difficulties.get(i))
                    .genreId((long) (i + 1))
                    .genreName(genres.get(i))
                    .build();

            kafkaProducerService.sendScoreEvent(event);
        }

        return "Multiple ScoreEvents sent.";
    }

    @PostMapping("/ai-recommendation-request")
    public String testAiRecommendationRequest() {
        AIRecommendationRequestEvent event = AIRecommendationRequestEvent.builder()
                .playerId(1L)
                .limit(5)
                .build();
        kafkaProducerService.sendAiRecommendationRequestEvent(event);
        return "AIRecommendationRequestEvent sent.";
    }

    @PostMapping("/ai-recommendation-response")
    public String testAiRecommendationResponse() {
        AIRecommendationResponseEvent event = AIRecommendationResponseEvent.builder()
                .playerId(1L)
                .recommendations(List.of(new RecommendationItem("Sample Genre", "Sample Difficulty", 0.5)))
                .generatedAt(LocalDateTime.now())
                .build();
        kafkaProducerService.sendAiRecommendationResponseEvent(event);
        return "AIRecommendationResponseEvent sent.";
    }
}
