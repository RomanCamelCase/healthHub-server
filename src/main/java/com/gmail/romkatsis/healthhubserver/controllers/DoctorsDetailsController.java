package com.gmail.romkatsis.healthhubserver.controllers;

import com.gmail.romkatsis.healthhubserver.dtos.embedded.ReviewDto;
import com.gmail.romkatsis.healthhubserver.dtos.embedded.SpecialisationDto;
import com.gmail.romkatsis.healthhubserver.dtos.requests.*;
import com.gmail.romkatsis.healthhubserver.dtos.responses.DoctorInfoResponse;
import com.gmail.romkatsis.healthhubserver.dtos.responses.DoctorsSearchResponse;
import com.gmail.romkatsis.healthhubserver.dtos.responses.TokensResponse;
import com.gmail.romkatsis.healthhubserver.services.DoctorsDetailsService;
import com.gmail.romkatsis.healthhubserver.services.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorsDetailsController {

    private final DoctorsDetailsService doctorsDetailsService;

    private final ReviewService reviewService;

    public DoctorsDetailsController(DoctorsDetailsService doctorsDetailsService, ReviewService reviewService) {
        this.doctorsDetailsService = doctorsDetailsService;
        this.reviewService = reviewService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public DoctorsSearchResponse findDoctors(@Valid DoctorSearchRequest searchRequest) {
        return doctorsDetailsService.findDoctorsBySearchRequest(searchRequest);
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
    public DoctorInfoResponse editDoctorSpecialisations(@PathVariable int id,
                                                    @RequestBody @Valid SpecialisationsRequest specialisations) {
        return doctorsDetailsService.editDoctorSpecialisations(id, specialisations);
    }

    @PostMapping("/{id}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public Set<ReviewDto> addDoctorReview(@AuthenticationPrincipal UserDetails userDetails,
                                          @PathVariable int id,
                                          @RequestBody @Valid ReviewRequest request) {
        return reviewService.addDoctorReview(
                Integer.parseInt(userDetails.getUsername()),
                id,
                request);
    }

    @GetMapping("/{id}/reviews")
    @ResponseStatus(HttpStatus.OK)
    public Set<ReviewDto> getDoctorReviews(@PathVariable int id) {
        return reviewService.getDoctorReview(id);
    }

    @GetMapping("/specialisations")
    @ResponseStatus(HttpStatus.OK)
    public Set<SpecialisationDto> getDoctorsSpecialisationsList() {
        return doctorsDetailsService.getDoctorsSpecialisations();
    }
}
