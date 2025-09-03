package com.capornocap.utils;

public enum Genre {
    ENTERTAINMENT,
    FASHION,
    GAMING,
    GENERAL_KNOWLEDGE,
    GEOGRAPHY,
    HISTORY,
    LITERATURE,
    MATHEMATICS,
    MEMES,
    MUSIC,
    SCIENCE,
    SPORTS,
    TECHNOLOGY;

    public static Genre fromString(String genre) {
        for (Genre g : Genre.values()) {
            if (g.name().equalsIgnoreCase(genre)) {
                return g;
            }
        }
        throw new IllegalArgumentException("No enum constant for genre: " + genre);
    }
}
