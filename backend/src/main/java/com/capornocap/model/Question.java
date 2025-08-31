package com.capornocap.model;

import com.capornocap.utils.Difficulty;
import com.capornocap.utils.Genre;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String questionText;

    @NotNull
    private Boolean isCap;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Genre genre;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Difficulty difficulty;
}
