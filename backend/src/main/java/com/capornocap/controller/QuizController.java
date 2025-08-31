package com.capornocap.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capornocap.dto.QuizSubmitRequest;
import com.capornocap.dto.QuizSubmitResponse;
import com.capornocap.service.QuizService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class QuizController {
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/submitQuiz")
    public ResponseEntity<QuizSubmitResponse> submitQuiz(@RequestBody QuizSubmitRequest request) {
        try {
            QuizSubmitResponse response = this.quizService.submitQuiz(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
