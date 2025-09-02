package com.capornocap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capornocap.entity.Question;
import com.capornocap.utils.Difficulty;
import com.capornocap.utils.Genre;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByGenreAndDifficulty(Genre genre, Difficulty difficulty);
}
