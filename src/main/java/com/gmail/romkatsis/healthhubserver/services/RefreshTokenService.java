package com.gmail.romkatsis.healthhubserver.services;

import com.gmail.romkatsis.healthhubserver.exceptions.InvalidTokenException;
import com.gmail.romkatsis.healthhubserver.models.RefreshToken;
import com.gmail.romkatsis.healthhubserver.models.User;
import com.gmail.romkatsis.healthhubserver.repositories.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final int refreshTokenDuration;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               @Value("${auth.tokens.refresh-token.duration}") int refreshTokenDuration) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenDuration = refreshTokenDuration;
    }

    @Transactional
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken(
                UUID.randomUUID().toString(),
                LocalDateTime.now().plus(refreshTokenDuration, ChronoUnit.MILLIS),
                user);
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Transactional
    public User verifyToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(() ->
                new InvalidTokenException("Unable to validate refresh token"));

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new InvalidTokenException("The refresh token is no longer valid");
        }

        User user = refreshToken.getUser();
        refreshTokenRepository.delete(refreshToken);
        return user;
    }

}
