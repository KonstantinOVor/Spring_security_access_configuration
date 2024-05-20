package com.example.Spring_security_access_configuration.service;

import com.example.Spring_security_access_configuration.exception.RefreshTokenException;
import com.example.Spring_security_access_configuration.entity.RefreshToken;
import com.example.Spring_security_access_configuration.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${jwt.refreshTokenExpirationTime}")
    private Duration refreshTokenExpirationTime;
    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> findByRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .expiryDate(Instant.now().plusMillis(refreshTokenExpirationTime.toMillis()))
                .token(UUID.randomUUID().toString())
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken checkRefreshToken(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException(token.getToken(), "Refresh-токен устарел");
        }
        return token;
    }

    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
