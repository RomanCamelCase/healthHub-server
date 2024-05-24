package com.gmail.romkatsis.healthhubserver.services;

import com.gmail.romkatsis.healthhubserver.dtos.LoginRequest;
import com.gmail.romkatsis.healthhubserver.dtos.TokensResponse;
import com.gmail.romkatsis.healthhubserver.models.RefreshToken;
import com.gmail.romkatsis.healthhubserver.models.User;
import com.gmail.romkatsis.healthhubserver.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    private final UserService userService;


    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, JwtUtils jwtUtils, RefreshTokenService refreshTokenService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
    }

    public TokensResponse authenticate(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String accessToken = jwtUtils.generateAccessToken(userDetails.getUsername(), userDetails.getAuthorities());
        User user = userService.findUserById(Integer.parseInt(userDetails.getUsername()));
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new TokensResponse(accessToken, refreshToken.getToken());
    }

    public TokensResponse refreshUserTokens(String refreshToken) {
        User user = refreshTokenService.verifyToken(refreshToken);

        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);
        String newAccessToken =
                jwtUtils.generateAccessToken(user.getId().toString(), user.getRoles());
        return new TokensResponse(newAccessToken, newRefreshToken.getToken());
    }
}
