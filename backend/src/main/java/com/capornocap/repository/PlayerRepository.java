package com.capornocap.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.capornocap.entity.Player;
import com.capornocap.model.Leaderboard;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT new com.capornocap.model.Leaderboard(p.username, p.xp, p.avatarUrl) FROM Player p ORDER BY p.xp DESC")
    Page<Leaderboard> getLeaderboard(Pageable pageable);
}
