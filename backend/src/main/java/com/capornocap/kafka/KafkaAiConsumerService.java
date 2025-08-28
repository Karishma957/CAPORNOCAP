package com.capornocap.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.capornocap.dto.AIRecommendationRequestEvent;
import com.capornocap.dto.AIRecommendationResponseEvent;
import com.capornocap.kafka.event.ScoreEvent;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaAiConsumerService {
    @Value("${recommender.base-url}")
    private String recommenderBaseUrl;

    private final RestTemplate restTemplate;
    private final KafkaProducerService kafkaProducerService;
    private String RECOMMEND_API_URL;
    private String SCORE_API_URL;

    public KafkaAiConsumerService(RestTemplate restTemplate, KafkaProducerService KafkaProducerService) {
        this.restTemplate = restTemplate;
        this.kafkaProducerService = KafkaProducerService;
    }

    @PostConstruct
    public void initUrls() {
        this.RECOMMEND_API_URL = recommenderBaseUrl + "/recommend";
        this.SCORE_API_URL = recommenderBaseUrl + "/score_update";
    }

    @KafkaListener(topics = KafkaTopics.SCORE, groupId = KafkaConsumerGroups.AI_CONSUMER_GROUP_ID)
    public void aiGroupHandleScore(ScoreEvent scoreEvent) {
        System.out.println("AI Group has reciever SCORE_EVENT message: " + scoreEvent);
        try {
            restTemplate.postForObject(
                    SCORE_API_URL, scoreEvent, ScoreEvent.class);
            log.info("Score event successfully put");
        } catch (Exception e) {
            log.error("Error while calling Recommendation service", e);
        }
    }

    @KafkaListener(topics = KafkaTopics.AI_RECOMMENDATION_REQUEST, groupId = KafkaConsumerGroups.AI_CONSUMER_GROUP_ID)
    public void aiGroupHandleRecommendationRequest(AIRecommendationRequestEvent request) {
        log.info("Recieved AI Recommendation Request: " + request);
        try {
            AIRecommendationResponseEvent responseEvent = restTemplate.postForObject(
                    RECOMMEND_API_URL, request, AIRecommendationResponseEvent.class);
            kafkaProducerService.sendAiRecommendationResponseEvent(responseEvent);
        } catch (Exception e) {
            log.error("Error while calling Recommendation service", e);
        }
    }

    @KafkaListener(topics = KafkaTopics.AI_RECOMMENDATION_RESPONSE, groupId = KafkaConsumerGroups.AI_CONSUMER_GROUP_ID)
    public void aiGroupHandleRecommendationResponse(AIRecommendationResponseEvent response) {
        System.out.println("Recieved AI Recommendation Response: " + response);
    }
}
