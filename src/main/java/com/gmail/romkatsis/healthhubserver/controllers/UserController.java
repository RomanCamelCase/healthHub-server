package com.gmail.romkatsis.healthhubserver.controllers;

import com.gmail.romkatsis.healthhubserver.dtos.requests.RegistrationRequest;
import com.gmail.romkatsis.healthhubserver.dtos.requests.UpdateUserInfoRequest;
import com.gmail.romkatsis.healthhubserver.dtos.responses.CurrentUserResponse;
import com.gmail.romkatsis.healthhubserver.dtos.responses.DoctorsDetailsShortResponse;
import com.gmail.romkatsis.healthhubserver.dtos.responses.TokensResponse;
import com.gmail.romkatsis.healthhubserver.enums.Role;
import com.gmail.romkatsis.healthhubserver.models.DoctorsDetails;
import com.gmail.romkatsis.healthhubserver.models.DoctorsSpecialisation;
import com.gmail.romkatsis.healthhubserver.models.User;
import com.gmail.romkatsis.healthhubserver.services.AuthenticationService;
import com.gmail.romkatsis.healthhubserver.services.UserService;
import jakarta.validation.Valid;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
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
        User user = convertRegistrationRequestToUser(registrationRequest);
        userService.saveUser(user);
        return authenticationService.generateTokensByUser(user);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CurrentUserResponse getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findUserById(Integer.parseInt(userDetails.getUsername()));
        return modelMapper.map(user, CurrentUserResponse.class);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CurrentUserResponse editUserInfo(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestBody @Valid UpdateUserInfoRequest updateUserInfoRequest) {
        User user = userService.findUserById(Integer.parseInt(userDetails.getUsername()));
        modelMapper.map(updateUserInfoRequest, user);
        userService.updateUser(user);
        return modelMapper.map(user, CurrentUserResponse.class);
    }

    @PostMapping("/saved-doctors/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addSavedDoctor(@PathVariable int id,
                               @AuthenticationPrincipal UserDetails userDetails) {
        userService.addDoctorToSaved(Integer.parseInt(userDetails.getUsername()), id);
    }

    @DeleteMapping("/saved-doctors/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeSavedDoctor(@PathVariable int id,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        userService.removeDoctorFromSaved(Integer.parseInt(userDetails.getUsername()), id);
    }

    @GetMapping("/saved-doctors")
    @ResponseStatus(HttpStatus.OK)
    public Set<DoctorsDetailsShortResponse> getAllSavedDoctors(@AuthenticationPrincipal UserDetails userDetails) {
        Set<DoctorsDetails> doctorsDetails = userService.getSavedDoctorsByUserId(
                Integer.parseInt(userDetails.getUsername()));
        return doctorsDetails.stream()
                .map(this::convertDoctorsDetailsToDoctorsDetailsShortResponse)
                .collect(Collectors.toSet());
    }

    private User convertRegistrationRequestToUser(RegistrationRequest registrationRequest) {
        User user = modelMapper.map(registrationRequest, User.class);
        user.setRegistrationDate(LocalDate.now());
        user.addRole(Role.ROLE_CUSTOMER);
        return user;
    }

    private DoctorsDetailsShortResponse convertDoctorsDetailsToDoctorsDetailsShortResponse(DoctorsDetails doctorsDetails) {
        Converter<Collection<DoctorsSpecialisation>, Collection<Integer>> converter = c ->
                c.getSource().stream().map(DoctorsSpecialisation::getId).collect(Collectors.toSet());

        modelMapper.typeMap(DoctorsDetails.class, DoctorsDetailsShortResponse.class)
                .addMapping(details -> details.getUser().getFirstName(),
                        DoctorsDetailsShortResponse::setFirstName)
                .addMapping(details -> details.getUser().getLastName(),
                        DoctorsDetailsShortResponse::setLastName)
                .addMappings(mapper -> mapper.using(converter)
                        .map(DoctorsDetails::getDoctorsSpecialisations,
                                DoctorsDetailsShortResponse::setSpecialisations));
        return modelMapper.map(doctorsDetails, DoctorsDetailsShortResponse.class);
    }
}
