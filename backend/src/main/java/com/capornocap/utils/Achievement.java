package com.capornocap.utils;

import lombok.Getter;

@Getter
public enum Achievement {

    NEWBIE("Newbie", "Started your quiz journey!", 0),
    FIRST_QUIZ("First Quiz", "Completed your first quiz!", 10),
    GETTING_STARTED("Getting Started", "Earned 50 XP in total!", 50),
    QUIZ_EXPLORER("Quiz Explorer", "Completed 5 quizzes cumulatively!", 100),
    PERFECTIONIST("Perfectionist", "Scored 100% in a quiz!", 150),
    QUIZ_MASTER("Quiz Master", "Reached 300 XP in total!", 300),
    GENIUS("Genius", "Consistently top scoring player!", 500),
    LEGEND("Legend", "Reached 1000 XP in total!", 1000),
    UNSTOPPABLE("Unstoppable", "Completed 50 quizzes cumulatively!", 1500),
    ULTIMATE_CHAMPION("Ultimate Champion", "Reached 3000 XP!", 3000),
    QUIZ_LEGEND("Quiz Legend", "Reached 5000 XP!", 5000),
    GRAND_MASTER("Grand Master", "Reached 10000 XP!", 10000);

    private final String title;
    private final String description;
    private final int xpThreshold;

    Achievement(String title, String description, int xpThreshold) {
        this.title = title;
        this.description = description;
        this.xpThreshold = xpThreshold;
    }

    public static Achievement getAchievementForXp(int totalXp) {
        Achievement result = NEWBIE;
        for (Achievement ach : Achievement.values()) {
            if (totalXp >= ach.getXpThreshold()) {
                result = ach;
            } else {
                break;
            }
        }
        return result;
    }

    public static Achievement getAchievementFromTitle(String title) {
        for (Achievement ach : Achievement.values()) {
            if (ach.getTitle().equalsIgnoreCase(title)) {
                return ach;
            }
        }
        throw new IllegalArgumentException("No achievement found for title: " + title);
    }
}
