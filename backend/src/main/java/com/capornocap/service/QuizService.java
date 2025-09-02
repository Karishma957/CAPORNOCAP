package com.capornocap.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.capornocap.dto.AchievementDTO;
import com.capornocap.dto.QuizAnswer;
import com.capornocap.dto.QuizSubmitRequest;
import com.capornocap.dto.QuizSubmitResponse;
import com.capornocap.kafka.KafkaProducerService;
import com.capornocap.kafka.event.ScoreEvent;
import com.capornocap.model.Answer;
import com.capornocap.model.Player;
import com.capornocap.model.Question;
import com.capornocap.model.Quiz;
import com.capornocap.repository.PlayerRepository;
import com.capornocap.repository.QuizRepository;
import com.capornocap.utils.Achievement;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuizService {
    private final QuizRepository quizRepository;
    private final PlayerRepository playerRepository;
    private final QuestionService questionService;
    private final KafkaProducerService kafkaProducerService;

    public QuizService(QuizRepository quizRepository, PlayerRepository playerRepository,
            QuestionService questionService, KafkaProducerService kafkaProducerService) {
        this.playerRepository = playerRepository;
        this.quizRepository = quizRepository;
        this.questionService = questionService;
        this.kafkaProducerService = kafkaProducerService;
    }

    public QuizSubmitResponse submitQuiz(QuizSubmitRequest request) {
        log.info("Submitting quiz for playerId: {}, genre: {}, difficulty: {}",
                request.getPlayerId(), request.getGenre(), request.getDifficulty());

        Player player = playerRepository.findById(request.getPlayerId())
                .orElseThrow(() -> new RuntimeException("Player not found"));
        log.info("Found player: {} with current XP: {}", player.getUsername(), player.getXp());

        java.util.concurrent.atomic.AtomicInteger score = new java.util.concurrent.atomic.AtomicInteger(0);

        List<Answer> answers = request.getAnswers().stream().map((QuizAnswer ans) -> {
            Optional<Question> questionOptional = this.questionService.getQuestionForId(ans.getQuestionId());
            if (questionOptional.isEmpty()) {
                log.warn("Question not found for questionId: {}", ans.getQuestionId());
                throw new RuntimeException("Question not found");
            }
            Question question = questionOptional.get();
            boolean correct = question.getIsCap() == ans.getAnswer();
            if (correct) {
                score.incrementAndGet();
            }
            log.info("QuestionId: {}, Answer given: {}, Correct: {}",
                    ans.getQuestionId(), ans.getAnswer(), correct);

            return Answer.builder()
                    .questionId(ans.getQuestionId())
                    .answer(ans.getAnswer())
                    .isCorrect(correct)
                    .build();
        }).collect(Collectors.toList());

        Quiz quiz = Quiz.builder()
                .playerId(request.getPlayerId())
                .genre(request.getGenre())
                .difficulty(request.getDifficulty())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .answers(answers)
                .score(score.get())
                .build();
        log.info("Saving quiz attempt with score: {}", score.get());
        Quiz q = this.quizRepository.save(quiz);

        int updatedXp = player.getXp() + score.get();
        log.info("Updating player XP: {} -> {}", player.getXp(), updatedXp);
        player.setXp(updatedXp);

        Achievement updAchievement = Achievement.getAchievementForXp(updatedXp);
        if (!updAchievement.equals(player.getAchievement())) {
            player.setAchievement(updAchievement);
        } else {
            updAchievement = null;
        }

        this.playerRepository.save(player);

        QuizSubmitResponse response = QuizSubmitResponse.builder()
                .score(score.get())
                .totalQuestions(answers.size())
                .achievement(updAchievement == null ? null
                        : AchievementDTO.builder()
                                .title(updAchievement.getTitle())
                                .description(updAchievement.getDescription())
                                .build())
                .answers(answers)
                .build();

        log.info("Quiz submission response: {}", response);

        ScoreEvent event = ScoreEvent.builder()
                .playerId(request.getPlayerId())
                .quizSessionId(q.getId())
                .score(response.getScore())
                .totalQuestions(response.getTotalQuestions())
                .correctAnswers(response.getScore())
                .difficulty(request.getDifficulty())
                .genreName(request.getGenre())
                .build();

        kafkaProducerService.sendScoreEvent(event);

        return response;
    }

}
