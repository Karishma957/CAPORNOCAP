package com.capornocap.model;

import com.capornocap.utils.Difficulty;
import com.capornocap.utils.Genre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoresCombo {
    public Genre genre;
    public Difficulty difficulty;
    public long plays;
    public double averageScore;
}
