package com.capornocap.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.capornocap.dto.AuthResponse;
import com.capornocap.model.Player;
import com.capornocap.repository.PlayerRepository;
import com.capornocap.utils.JwtUtil;

@Service
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
        if (opt == null) {
            Player newPlayer = Player.builder()
                    .username(username)
                    .passwordHash(passwordEncoder.encode(password))
                    .xp(0)
                    .build();
            newPlayer = playerRepository.save(newPlayer);
            String token = jwtUtil.generateToken(newPlayer.getUsername(), newPlayer.getId());
            return new AuthResponse(newPlayer.getId(), newPlayer.getUsername(), token);
        } else {
            Player existingPlayer = opt.get();
            if (passwordEncoder.matches(password, existingPlayer.getPasswordHash())) {
                String token = jwtUtil.generateToken(existingPlayer.getUsername(), existingPlayer.getId());
                return new AuthResponse(existingPlayer.getId(), existingPlayer.getUsername(), token);
            } else {
                throw new IllegalArgumentException("Username already exists with a different password");
            }
        }

    }
}
