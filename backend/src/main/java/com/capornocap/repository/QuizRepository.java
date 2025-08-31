package com.capornocap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capornocap.model.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
