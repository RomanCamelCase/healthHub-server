package com.gmail.romkatsis.healthhubserver.controllers;

import com.gmail.romkatsis.healthhubserver.dtos.requests.ContactRequest;
import com.gmail.romkatsis.healthhubserver.dtos.requests.DoctorInfoRequest;
import com.gmail.romkatsis.healthhubserver.dtos.requests.DoctorStatusRequest;
import com.gmail.romkatsis.healthhubserver.dtos.responses.DoctorInfoResponse;
import com.gmail.romkatsis.healthhubserver.dtos.responses.TokensResponse;
import com.gmail.romkatsis.healthhubserver.models.*;
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

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.Set;
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
                                           @RequestBody @Valid DoctorInfoRequest doctorInfoRequest) {
        DoctorsDetails doctorsDetails = convertDoctorsDetailsRequestToDoctorsDetails(doctorInfoRequest);
        User user = userService.findUserById(Integer.parseInt(userDetails.getUsername()));
        doctorsDetailsService.addDoctorDetails(doctorsDetails, user);
        return authenticationService.generateTokensByUser(user);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DoctorInfoResponse getDoctorsDetailsPublicInfo(@PathVariable int id) {
        DoctorsDetails doctorsDetails = doctorsDetailsService.findDoctorsDetailsById(id);
        return convertDoctorsDetailsToDoctorsDetailsPublicResponse(doctorsDetails);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#id == new Integer(principal.username)")
    public DoctorInfoResponse editDoctorsDetailsInfo(@PathVariable int id,
                                                     @RequestBody @Valid DoctorInfoRequest request) {
        DoctorsDetails doctorsDetails = doctorsDetailsService.findDoctorsDetailsById(id);
        modelMapper.map(request, doctorsDetails);
        doctorsDetails.setDoctorsSpecialisations(
                doctorsSpecialisationService.getDoctorsSpecialisationsSetByIds(request.getSpecialisations()));
        doctorsDetailsService.editDoctorDetails(doctorsDetails);
        return convertDoctorsDetailsToDoctorsDetailsPublicResponse(doctorsDetails);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("#id == new Integer(principal.username)")
    public DoctorInfoResponse updateDoctorsDetailsStatus(@PathVariable int id,
                                                         @RequestBody @Valid DoctorStatusRequest request) {
        DoctorsDetails doctorsDetails = doctorsDetailsService.findDoctorsDetailsById(id);
        doctorsDetails.setActive(request.isActive());
        doctorsDetailsService.editDoctorDetails(doctorsDetails);
        return convertDoctorsDetailsToDoctorsDetailsPublicResponse(doctorsDetails);
    }

    @PostMapping("/{id}/contacts")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#id == new Integer(principal.username)")
    public Set<DoctorsContact> addDoctorsContact(@RequestBody @Valid ContactRequest contactRequest,
                                                 @PathVariable int id) {
        return doctorsDetailsService.addDoctorsContact(id, modelMapper.map(contactRequest, DoctorsContact.class));
    }

    @DeleteMapping("/{id}/contacts")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#id == new Integer(principal.username)")
    public Set<DoctorsContact> deleteDoctorsContact(@PathVariable int id, @RequestParam int contactId) {
        return doctorsDetailsService.deleteDoctorsContact(id, contactId);
    }

    @PostMapping("/{id}/working-days")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#id == new Integer(principal.username)")
    public Set<WorkingDay> addWorkingDay(@PathVariable int id,
                                         @RequestBody @Valid WorkingDay workingDay) {
        return doctorsDetailsService.addWorkingDay(id, workingDay);
    }

    @DeleteMapping("/{id}/working-days")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#id == new Integer(principal.username)")
    public Set<WorkingDay> deleteDoctorsWorkingDay(@PathVariable int id, @RequestParam DayOfWeek dayOfWeek) {
        return doctorsDetailsService.deleteDoctorsWorkingDay(id, dayOfWeek);
    }

    private DoctorsDetails convertDoctorsDetailsRequestToDoctorsDetails(DoctorInfoRequest request) {
        DoctorsDetails doctorsDetails = modelMapper.map(request, DoctorsDetails.class);
        doctorsDetails.setDoctorsSpecialisations(
                doctorsSpecialisationService.getDoctorsSpecialisationsSetByIds(request.getSpecialisations()));
        return doctorsDetails;
    }

    private DoctorInfoResponse convertDoctorsDetailsToDoctorsDetailsPublicResponse(DoctorsDetails doctorsDetails) {
        Converter<Collection<DoctorsSpecialisation>, Collection<Integer>> converter = c ->
                c.getSource().stream().map(DoctorsSpecialisation::getId).collect(Collectors.toSet());

        modelMapper.typeMap(DoctorsDetails.class, DoctorInfoResponse.class)
                .addMapping(details -> details.getUser().getFirstName(),
                        DoctorInfoResponse::setFirstName)
                .addMapping(details -> details.getUser().getLastName(),
                        DoctorInfoResponse::setLastName)
                .addMapping(details -> details.getUser().getGender(),
                        DoctorInfoResponse::setGender)
                .addMapping(details -> details.getUser().getRegistrationDate(),
                        DoctorInfoResponse::setRegistrationDate)
                .addMappings(mapper -> mapper.using(converter)
                        .map(DoctorsDetails::getDoctorsSpecialisations,
                                DoctorInfoResponse::setSpecialisations));
        return modelMapper.map(doctorsDetails, DoctorInfoResponse.class);
    }
}
