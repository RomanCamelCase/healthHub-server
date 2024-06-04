package com.gmail.romkatsis.healthhubserver.controllers;

import com.gmail.romkatsis.healthhubserver.dtos.requests.DoctorsDetailsRequest;
import com.gmail.romkatsis.healthhubserver.dtos.requests.DoctorsStatusRequest;
import com.gmail.romkatsis.healthhubserver.dtos.responses.DoctorsDetailsResponse;
import com.gmail.romkatsis.healthhubserver.dtos.responses.TokensResponse;
import com.gmail.romkatsis.healthhubserver.models.DoctorsDetails;
import com.gmail.romkatsis.healthhubserver.models.DoctorsSpecialisation;
import com.gmail.romkatsis.healthhubserver.models.User;
import com.gmail.romkatsis.healthhubserver.services.AuthenticationService;
import com.gmail.romkatsis.healthhubserver.services.DoctorsDetailsService;
import com.gmail.romkatsis.healthhubserver.services.DoctorsSpecialisationService;
import com.gmail.romkatsis.healthhubserver.services.UserService;
import jakarta.validation.Valid;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorsDetailsController {

    private final DoctorsDetailsService doctorsDetailsService;

    private final DoctorsSpecialisationService doctorsSpecialisationService;

    private final UserService userService;

    private final AuthenticationService authenticationService;

    private final ModelMapper modelMapper;

    public DoctorsDetailsController(DoctorsDetailsService doctorsDetailsService, DoctorsSpecialisationService doctorsSpecialisationService,
                                    UserService userService,
                                    ModelMapper modelMapper,
                                    AuthenticationService authenticationService) {
        this.doctorsDetailsService = doctorsDetailsService;
        this.doctorsSpecialisationService = doctorsSpecialisationService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TokensResponse addDoctorDetails(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody @Valid DoctorsDetailsRequest doctorsDetailsRequest) {
        DoctorsDetails doctorsDetails = convertDoctorsDetailsRequestToDoctorsDetails(doctorsDetailsRequest);
        User user = userService.findUserById(Integer.parseInt(userDetails.getUsername()));
        doctorsDetailsService.addDoctorDetails(doctorsDetails, user);
        return authenticationService.generateTokensByUser(user);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DoctorsDetailsResponse getDoctorsDetailsPublicInfo(@PathVariable int id) {
        DoctorsDetails doctorsDetails = doctorsDetailsService.findDoctorsDetailsById(id);
        return convertDoctorsDetailsToDoctorsDetailsPublicResponse(doctorsDetails);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#id == new Integer(principal.username)")
    public DoctorsDetailsResponse editDoctorsDetailsInfo(@PathVariable int id,
                                                         @RequestBody @Valid DoctorsDetailsRequest request) {
        DoctorsDetails doctorsDetails = doctorsDetailsService.findDoctorsDetailsById(id);
        modelMapper.map(request, doctorsDetails);
        doctorsDetails.setDoctorsSpecialisations(
                doctorsSpecialisationService.getDoctorsSpecialisationsSetByIds(request.getSpecialisations()));
        doctorsDetailsService.editDoctorDetails(doctorsDetails);
        return convertDoctorsDetailsToDoctorsDetailsPublicResponse(doctorsDetails);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#id == new Integer(principal.username)")
    public DoctorsDetailsResponse updateDoctorsDetailsStatus(@PathVariable int id,
                                                             @RequestBody @Valid DoctorsStatusRequest request) {
        DoctorsDetails doctorsDetails = doctorsDetailsService.findDoctorsDetailsById(id);
        doctorsDetails.setActive(request.isActive());
        doctorsDetailsService.editDoctorDetails(doctorsDetails);
        return convertDoctorsDetailsToDoctorsDetailsPublicResponse(doctorsDetails);
    }

    private DoctorsDetails convertDoctorsDetailsRequestToDoctorsDetails(DoctorsDetailsRequest request) {
        DoctorsDetails doctorsDetails = modelMapper.map(request, DoctorsDetails.class);
        doctorsDetails.setDoctorsSpecialisations(
                doctorsSpecialisationService.getDoctorsSpecialisationsSetByIds(request.getSpecialisations()));
        return doctorsDetails;
    }

    private DoctorsDetailsResponse convertDoctorsDetailsToDoctorsDetailsPublicResponse(DoctorsDetails doctorsDetails) {
        Converter<Collection<DoctorsSpecialisation>, Collection<Integer>> converter = cnvrtr ->
                cnvrtr.getSource().stream().map(DoctorsSpecialisation::getId).collect(Collectors.toSet());

        modelMapper.typeMap(DoctorsDetails.class, DoctorsDetailsResponse.class)
                .addMapping(details -> details.getUser().getFirstName(),
                        DoctorsDetailsResponse::setFirstName)
                .addMapping(details -> details.getUser().getLastName(),
                        DoctorsDetailsResponse::setLastName)
                .addMapping(details -> details.getUser().getGender(),
                        DoctorsDetailsResponse::setGender)
                .addMapping(details -> details.getUser().getRegistrationDate(),
                        DoctorsDetailsResponse::setRegistrationDate)
                .addMappings(mapper -> mapper.using(converter)
                        .map(DoctorsDetails::getDoctorsSpecialisations,
                                DoctorsDetailsResponse::setSpecialisations));
        return modelMapper.map(doctorsDetails, DoctorsDetailsResponse.class);
    }
}
