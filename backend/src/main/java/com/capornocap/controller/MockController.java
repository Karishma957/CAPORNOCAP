package com.capornocap.controller;

import com.capornocap.dto.DeveloperConsoleState;
import com.capornocap.model.*;
import com.capornocap.service.DeveloperConsoleService;
import com.capornocap.utils.Achievement;
import com.capornocap.utils.Difficulty;
import com.capornocap.utils.Genre;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mock")
@RequiredArgsConstructor
public class MockController {

    private final DeveloperConsoleService developerConsoleService;

    @PostMapping("/login/{playerId}")
    public String mockLogin(@PathVariable Long playerId) {
        developerConsoleService.onLogin(playerId);
        return "Mock login event triggered for player " + playerId;
    }

    @PostMapping("/logout/{playerId}")
    public String mockLogout(@PathVariable Long playerId) {
        developerConsoleService.onLogout(playerId);
        return "Mock logout event triggered for player " + playerId;
    }

    @PostMapping("/quiz/start/{playerId}")
    public String mockQuizStart(@PathVariable Long playerId) {
        developerConsoleService.onQuizStarted(playerId);
        return "Mock quiz start event triggered for player " + playerId;
    }

    @PostMapping("/quiz/end/{playerId}")
    public String mockQuizEnd(@PathVariable Long playerId) {
        developerConsoleService.onQuizEnded(playerId);
        return "Mock quiz end event triggered for player " + playerId;
    }

    @PostMapping("/score")
    public String mockScore(
            @RequestParam Genre genre,
            @RequestParam Difficulty difficulty,
            @RequestParam int score) {
        developerConsoleService.onScore(genre, difficulty, score);
        return "Mock score event: " + genre + " - " + difficulty + " - " + score;
    }

    @PostMapping("/achievement")
    public String mockAchievement(@RequestParam Achievement achievement) {
        developerConsoleService.onAchievementUnlocked(achievement);
        return "Mock achievement unlocked: " + achievement;
    }

    @PostMapping("/recommendation")
    public String mockRecommendationAccepted() {
        developerConsoleService.onRecommendationAccepted();
        return "Mock recommendation accepted event triggered.";
    }

    @PostMapping("/totalPlayers/{count}")
    public String mockTotalPlayers(@PathVariable long count) {
        developerConsoleService.setTotalPlayers(count);
        return "Mock total players set to " + count;
    }

    @GetMapping("/snapshot")
    public DeveloperConsoleState snapshot() {
        return developerConsoleService.snapshot();
    }
}
