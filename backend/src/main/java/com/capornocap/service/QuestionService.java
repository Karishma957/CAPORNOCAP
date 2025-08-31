package com.capornocap.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.capornocap.dto.QuestionDTO;
import com.capornocap.model.Question;
import com.capornocap.repository.QuestionRepository;
import com.capornocap.utils.Difficulty;
import com.capornocap.utils.Genre;

@Service
public class QuestionService {
    private QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> saveQuestions(List<Question> questions) {
        return this.questionRepository.saveAll(questions);
    }

    public List<QuestionDTO> getQuestions(Genre genre, Difficulty difficulty) {
        List<Question> allQuestions = this.questionRepository.findByGenreAndDifficulty(genre, difficulty);
        Collections.shuffle(allQuestions);
        return allQuestions.stream()
                .limit(5)
                .map(q -> new QuestionDTO(q.getId(), q.getQuestionText()))
                .collect(Collectors.toList());
    }
}
