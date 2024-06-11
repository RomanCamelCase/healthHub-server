package com.gmail.romkatsis.healthhubserver.services;

import com.gmail.romkatsis.healthhubserver.dtos.requests.LoginRequest;
import com.gmail.romkatsis.healthhubserver.dtos.requests.TokenRequest;
import com.gmail.romkatsis.healthhubserver.dtos.responses.TokensResponse;
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
        User user = userService.findUserById(Integer.parseInt(userDetails.getUsername()));
        return generateTokensByUser(user);
    }

    public TokensResponse refreshUserTokens(TokenRequest refreshTokenRequest) {
        User user = refreshTokenService.verifyToken(refreshTokenRequest.getToken());
        return generateTokensByUser(user);
    }

    public void logOut(TokenRequest refreshTokenRequest, boolean fromAllDevices) {
        User user = refreshTokenService.verifyToken(refreshTokenRequest.getToken());

        if (fromAllDevices) {
            refreshTokenService.removeAllTokensByUser(user);
        }
    }

    public TokensResponse generateTokensByUser(User user) {
        String accessToken = jwtUtils.generateAccessToken(user.getId().toString(), user.getRoles());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        return new TokensResponse(accessToken, refreshToken.getToken());

    }
}
