package com.gmail.romkatsis.healthhubserver.controllers;

import com.gmail.romkatsis.healthhubserver.dtos.requests.*;
import com.gmail.romkatsis.healthhubserver.dtos.responses.DoctorInfoResponse;
import com.gmail.romkatsis.healthhubserver.dtos.responses.TokensResponse;
import com.gmail.romkatsis.healthhubserver.services.DoctorsDetailsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorsDetailsController {

    private final DoctorsDetailsService doctorsDetailsService;

    public DoctorsDetailsController(DoctorsDetailsService doctorsDetailsService) {
        this.doctorsDetailsService = doctorsDetailsService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TokensResponse addDoctorDetails(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestBody @Valid DoctorInfoRequest doctorInfoRequest) {
        return doctorsDetailsService.addDoctorDetails(Integer.parseInt(userDetails.getUsername()), doctorInfoRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DoctorInfoResponse getDoctorsDetailsPublicInfo(@PathVariable int id) {
        return doctorsDetailsService.getDoctorInfoById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#id == new Integer(principal.username)")
    public DoctorInfoResponse editDoctorsDetailsInfo(@PathVariable int id,
                                                     @RequestBody @Valid DoctorInfoRequest request) {
        return doctorsDetailsService.editDoctorInfo(id, request);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#id == new Integer(principal.username)")
    public DoctorInfoResponse updateDoctorsDetailsStatus(@PathVariable int id,
                                                         @RequestBody @Valid DoctorStatusRequest request) {
        return doctorsDetailsService.editDoctorStatus(id, request);
    }

    @PostMapping("/{id}/contacts")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("#id == new Integer(principal.username)")
    public DoctorInfoResponse addDoctorsContact(@RequestBody @Valid ContactRequest contactRequest,
                                                @PathVariable int id) {
        return doctorsDetailsService.addDoctorContact(id, contactRequest);
    }

    @DeleteMapping("/{id}/contacts")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#id == new Integer(principal.username)")
    public DoctorInfoResponse deleteDoctorsContact(@PathVariable int id, @RequestParam int contactId) {
        return doctorsDetailsService.removeDoctorContact(id, contactId);
    }

    @PutMapping("/{id}/working-days")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#id == new Integer(principal.username)")
    public DoctorInfoResponse editDoctorWorkingDays(@PathVariable int id,
                                                    @RequestBody @Valid WorkingDaysRequest workingDays) {
        return doctorsDetailsService.editDoctorWorkingDays(id, workingDays);
    }

    @PutMapping("/{id}/specialisations")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#id == new Integer(principal.username)")
    public DoctorInfoResponse editDoctorWorkingDays(@PathVariable int id,
                                                    @RequestBody @Valid SpecialisationsRequest specialisations) {
        return doctorsDetailsService.editDoctorSpecialisations(id, specialisations);
    }
}
