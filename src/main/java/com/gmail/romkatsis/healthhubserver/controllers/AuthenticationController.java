package com.gmail.romkatsis.healthhubserver.controllers;

import com.gmail.romkatsis.healthhubserver.dtos.requests.ChangePasswordRequest;
import com.gmail.romkatsis.healthhubserver.dtos.requests.LoginRequest;
import com.gmail.romkatsis.healthhubserver.dtos.requests.TokenRequest;
import com.gmail.romkatsis.healthhubserver.dtos.responses.TokensResponse;
import com.gmail.romkatsis.healthhubserver.services.AuthenticationService;
import com.gmail.romkatsis.healthhubserver.services.AuthorityTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final AuthorityTokenService authorityTokenService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, AuthorityTokenService authorityTokenService) {
        this.authenticationService = authenticationService;
        this.authorityTokenService = authorityTokenService;
    }

    @PostMapping("/log-in")
    @ResponseStatus(HttpStatus.OK)
    public TokensResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        return authenticationService.authenticate(loginRequest);
    }

    @PostMapping("/refresh-tokens")
    @ResponseStatus(HttpStatus.OK)
    public TokensResponse refreshTokens(@RequestBody TokenRequest request) {
        return authenticationService.refreshUserTokens(request);
    }

    @PostMapping("/log-out")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestBody @Valid TokenRequest refreshTokenRequest,
                       @RequestParam(defaultValue = "false") boolean fromAllDevices) {
        authenticationService.logOut(refreshTokenRequest, fromAllDevices);
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public void resetPassword(@RequestParam String email) {
        authorityTokenService.sendResetPasswordEmail(email);
    }

    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    public void setNewPassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        authorityTokenService.changePassword(changePasswordRequest);
    }
}
