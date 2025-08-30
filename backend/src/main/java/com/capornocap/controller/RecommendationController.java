package com.capornocap.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.capornocap.dto.AIRecommendationRequestEvent;
import com.capornocap.dto.AIRecommendationResponseEvent;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
@Slf4j
public class RecommendationController {
    @Value("${recommender.base-url}")
    private String recommenderBaseUrl;

    private final RestTemplate restTemplate;

    public RecommendationController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/recommend/{playerId}")
    public ResponseEntity<AIRecommendationResponseEvent> getRecommendation(@PathVariable Long playerId) {
        try {
            String recommendApiUrl = recommenderBaseUrl + "/recommend";
            log.info("Calling recommender for playerId={} at {}", playerId, recommendApiUrl);
            AIRecommendationRequestEvent request = AIRecommendationRequestEvent.builder()
                    .playerId(playerId)
                    .limit(5)
                    .build();
            AIRecommendationResponseEvent response = restTemplate.postForObject(recommendApiUrl, request,
                    AIRecommendationResponseEvent.class);
            log.info("Got response: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error calling recommender API", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
