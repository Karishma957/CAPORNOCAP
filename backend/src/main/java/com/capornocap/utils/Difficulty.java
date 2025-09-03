package com.capornocap.utils;

public enum Difficulty {
    EASY,
    MEDIUM,
    HARD;

    public static Difficulty fromString(String difficulty) {
        for (Difficulty d : Difficulty.values()) {
            if (d.name().equalsIgnoreCase(difficulty)) {
                return d;
            }
        }
        throw new IllegalArgumentException("No enum constant for difficulty: " + difficulty);
    }
}
