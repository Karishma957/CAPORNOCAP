package com.capornocap.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.capornocap.dto.Leaderboard;
import com.capornocap.model.Player;
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

    public List<Leaderboard> getLeaderboard() {
        return this.playerRepository.getLeaderboard();
    }
}
