package com.capornocap.service;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.capornocap.dto.AuthResponse;
import com.capornocap.model.Player;
import com.capornocap.repository.PlayerRepository;
import com.capornocap.utils.Achievement;
import com.capornocap.utils.JwtUtil;

@Service
@Slf4j
public class AuthService {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(PlayerRepository playerRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse registerOrLogin(String username, String password) {

        Optional<Player> opt = playerRepository.findByUsername(username);
        log.info("Player lookup result for username {}: {}", username, opt);

        if (opt.isEmpty()) {
            log.info("Username not found. Registering new player: {}", username);

            Player newPlayer = Player.builder()
                    .username(username)
                    .passwordHash(passwordEncoder.encode(password))
                    .xp(0)
                    .createdAt(Instant.now())
                    .achievement(Achievement.NEWBIE)
                    .build();

            newPlayer = playerRepository.save(newPlayer);
            log.info("New player registered with id: {}", newPlayer.getId());

            String token = jwtUtil.generateToken(newPlayer.getUsername(), newPlayer.getId());
            log.info("Generated JWT for new player: {}", username);

            return new AuthResponse(newPlayer.getId(), newPlayer.getUsername(), token);

        } else {
            Player existingPlayer = opt.get();
            log.info("Username '{}' already exists with id: {}", username, existingPlayer.getId());

            if (passwordEncoder.matches(password, existingPlayer.getPasswordHash())) {
                log.info("Password matched for username: {}", username);

                String token = jwtUtil.generateToken(existingPlayer.getUsername(), existingPlayer.getId());
                log.info("Generated JWT for existing player: {}", username);

                return new AuthResponse(existingPlayer.getId(), existingPlayer.getUsername(), token);
            } else {
                log.warn("Password mismatch for username: {}", username);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Username already exists with a different password");
            }
        }
    }
}
