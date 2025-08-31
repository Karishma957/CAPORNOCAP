package com.capornocap.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.capornocap.dto.Leaderboard;
import com.capornocap.model.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT new com.capornocap.dto.Leaderboard(p.username, p.xp, p.avatarUrl) FROM Player p ORDER BY p.xp DESC")
    List<Leaderboard> getLeaderboard();
}
