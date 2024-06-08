package com.gmail.romkatsis.healthhubserver.controllers;

import com.gmail.romkatsis.healthhubserver.dtos.requests.RegistrationRequest;
import com.gmail.romkatsis.healthhubserver.dtos.requests.UpdateUserInfoRequest;
import com.gmail.romkatsis.healthhubserver.dtos.responses.ClinicInfoShortResponse;
import com.gmail.romkatsis.healthhubserver.dtos.responses.CurrentUserInfoResponse;
import com.gmail.romkatsis.healthhubserver.dtos.responses.DoctorsDetailsInfoShortResponse;
import com.gmail.romkatsis.healthhubserver.dtos.responses.TokensResponse;
import com.gmail.romkatsis.healthhubserver.models.User;
import com.gmail.romkatsis.healthhubserver.services.AuthenticationService;
import com.gmail.romkatsis.healthhubserver.services.SavedDoctorsAndClinicsService;
import com.gmail.romkatsis.healthhubserver.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    private final AuthenticationService authenticationService;

    private final SavedDoctorsAndClinicsService savedDoctorsAndClinicsService;

    @Autowired
    public UserController(UserService userService, AuthenticationService authenticationService, SavedDoctorsAndClinicsService savedDoctorsAndClinicsService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.savedDoctorsAndClinicsService = savedDoctorsAndClinicsService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TokensResponse registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        User user = userService.saveUser(registrationRequest);
        return authenticationService.generateTokensByUser(user);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CurrentUserInfoResponse getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.getUserInfoById(Integer.parseInt(userDetails.getUsername()));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public CurrentUserInfoResponse editUserInfo(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestBody @Valid UpdateUserInfoRequest updateUserInfoRequest) {
        return userService.editUserInfo(Integer.parseInt(userDetails.getUsername()), updateUserInfoRequest);
    }

    @GetMapping("/saved-doctors")
    @ResponseStatus(HttpStatus.OK)
    public Set<DoctorsDetailsInfoShortResponse> getAllSavedDoctors(@AuthenticationPrincipal UserDetails userDetails) {
        return savedDoctorsAndClinicsService.getSavedDoctorsByUserId(Integer.parseInt(userDetails.getUsername()));
    }

    @PostMapping("/saved-doctors/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Set<DoctorsDetailsInfoShortResponse> addSavedDoctor(@PathVariable int id,
                                                               @AuthenticationPrincipal UserDetails userDetails) {
         return savedDoctorsAndClinicsService.saveDoctor(Integer.parseInt(userDetails.getUsername()), id);
    }

    @DeleteMapping("/saved-doctors/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Set<DoctorsDetailsInfoShortResponse> removeSavedDoctor(@PathVariable int id,
                                                                  @AuthenticationPrincipal UserDetails userDetails) {
        return savedDoctorsAndClinicsService.deleteDoctor(Integer.parseInt(userDetails.getUsername()), id);
    }

    @GetMapping("/saved-clinics")
    @ResponseStatus(HttpStatus.OK)
    public Set<ClinicInfoShortResponse> getAllSavedClinics(@AuthenticationPrincipal UserDetails userDetails) {
        return savedDoctorsAndClinicsService.getSavedClinicsByUserId(Integer.parseInt(userDetails.getUsername()));
    }

    @PostMapping("/saved-clinics/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Set<ClinicInfoShortResponse> addSavedClinic(@PathVariable int id,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
         return savedDoctorsAndClinicsService.saveClinic(Integer.parseInt(userDetails.getUsername()), id);
    }

    @DeleteMapping("/saved-clinics/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Set<ClinicInfoShortResponse> removeSavedClinic(@PathVariable int id,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        return savedDoctorsAndClinicsService.deleteClinic(Integer.parseInt(userDetails.getUsername()), id);
    }
}
