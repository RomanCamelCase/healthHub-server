package com.gmail.romkatsis.healthhubserver.controllers;

import com.gmail.romkatsis.healthhubserver.dtos.requests.RegistrationRequest;
import com.gmail.romkatsis.healthhubserver.dtos.responses.TokensResponse;
import com.gmail.romkatsis.healthhubserver.models.User;
import com.gmail.romkatsis.healthhubserver.services.AuthenticationService;
import com.gmail.romkatsis.healthhubserver.services.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    private final AuthenticationService authenticationService;

    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, AuthenticationService authenticationService, ModelMapper modelMapper) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TokensResponse registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        User user = modelMapper.map(registrationRequest, User.class);
        userService.saveUser(user);
        return authenticationService.generateTokensByUser(user);
    }
}
