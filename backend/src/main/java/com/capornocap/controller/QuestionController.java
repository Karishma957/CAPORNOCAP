package com.capornocap.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capornocap.dto.QuestionDTO;
import com.capornocap.model.Question;
import com.capornocap.service.QuestionService;
import com.capornocap.utils.Difficulty;
import com.capornocap.utils.Genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
@Slf4j
public class QuestionController {
    private QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/bulkAdd")
    public ResponseEntity<List<Question>> addQuestions(@RequestBody List<Question> questions) {
        try {
            List<Question> response = this.questionService.saveQuestions(questions);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Could not add Questions: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/quiz")
    public ResponseEntity<List<QuestionDTO>> getQuiz(@RequestParam Genre genre, @RequestParam Difficulty difficulty) {
        try {
            List<QuestionDTO> response = this.questionService.getQuestions(genre, difficulty);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Could not get Questions: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
