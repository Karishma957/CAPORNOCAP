package com.capornocap.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capornocap.dto.AchievementDTO;
import com.capornocap.dto.LeaderboardDto;
import com.capornocap.dto.PlayerDTO;
import com.capornocap.entity.Player;
import com.capornocap.model.Leaderboard;
import com.capornocap.service.PlayerService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<LeaderboardDto> getLeaderboard(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Leaderboard> leaderboard = this.playerService.getLeaderboard(page, size);
            LeaderboardDto response = LeaderboardDto.builder()
                    .players(leaderboard.getContent())
                    .totalCount(leaderboard.getTotalElements())
                    .totalPages(leaderboard.getTotalPages())
                    .currentPage(leaderboard.getNumber())
                    .build();
            log.info("Got response: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error calling leaderboard API", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<PlayerDTO> getPlayer(@PathVariable Long playerId) {
        try {
            Optional<Player> response = this.playerService.getPlayer(playerId);
            if (response.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            Player player = response.get();
            PlayerDTO playerDTO = PlayerDTO.builder()
                    .id(player.getId())
                    .username(player.getUsername())
                    .xp(player.getXp())
                    .avatarUrl(player.getAvatarUrl())
                    .createdAt(player.getCreatedAt())
                    .achievement(
                            AchievementDTO.builder()
                                    .title(player.getAchievement().getTitle())
                                    .description(player.getAchievement().getDescription())
                                    .build())
                    .build();
            return ResponseEntity.ok(playerDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
