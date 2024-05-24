package com.gmail.romkatsis.healthhubserver.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;

@Component
public class JwtUtils {
    private final SecretKey accessSecretKey;

    private final int accessTokenDuration;

    @Autowired
    public JwtUtils(@Value("${auth.tokens.access-token.secret}") String accessTokenSecret,
                    @Value("${auth.tokens.access-token.duration}") int accessTokenDuration) {
        this.accessSecretKey = generateKey(accessTokenSecret);
        this.accessTokenDuration = accessTokenDuration;
    }

    private SecretKey generateKey(String secret) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateAccessToken(String userId, Collection<? extends GrantedAuthority> authorities) {
        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime() + accessTokenDuration);

        return Jwts.builder()
                .subject(userId)
                .claim("authorities", authorities)
                .issuedAt(issuedAt)
                .expiration(expiredAt)
                .signWith(accessSecretKey)
                .compact();
    }

    public Claims getClaims(String token) throws JwtException {
            return Jwts.parser()
                    .verifyWith(accessSecretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }
}
