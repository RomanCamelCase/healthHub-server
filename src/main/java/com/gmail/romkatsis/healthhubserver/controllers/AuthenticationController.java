package com.gmail.romkatsis.healthhubserver.controllers;

import com.gmail.romkatsis.healthhubserver.dtos.requests.LoginRequest;
import com.gmail.romkatsis.healthhubserver.dtos.requests.RefreshTokenRequest;
import com.gmail.romkatsis.healthhubserver.dtos.responses.TokensResponse;
import com.gmail.romkatsis.healthhubserver.services.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/log-in")
    @ResponseStatus(HttpStatus.OK)
    public TokensResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        return authenticationService.authenticate(loginRequest);
    }

    @PostMapping("/refresh-tokens")
    @ResponseStatus(HttpStatus.OK)
    public TokensResponse refreshTokens(@RequestBody RefreshTokenRequest request) {
        return authenticationService.refreshUserTokens(request);
    }

    @PostMapping("/log-out")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest,
                       @RequestParam(defaultValue = "false") boolean fromAllDevices) {
        authenticationService.logOut(refreshTokenRequest, fromAllDevices);
    }
}
