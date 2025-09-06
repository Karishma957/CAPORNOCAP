package com.capornocap.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.capornocap.entity.Player;
import com.capornocap.model.Leaderboard;
import com.capornocap.repository.PlayerRepository;

@Service
public class PlayerService {
    private PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Optional<Player> getPlayer(Long playerId) {
        return this.playerRepository.findById(playerId);
    }

    public Page<Leaderboard> getLeaderboard(int page, int size) {
        return this.playerRepository.getLeaderboard(PageRequest.of(page, size));
    }

    public Player loadByUsername(String username) {
        return playerRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
