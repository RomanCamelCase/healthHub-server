package com.gmail.romkatsis.healthhubserver.controllers;

import com.gmail.romkatsis.healthhubserver.dtos.embedded.ClinicAmenityDto;
import com.gmail.romkatsis.healthhubserver.dtos.embedded.ReviewDto;
import com.gmail.romkatsis.healthhubserver.dtos.embedded.SpecialisationDto;
import com.gmail.romkatsis.healthhubserver.dtos.requests.*;
import com.gmail.romkatsis.healthhubserver.dtos.responses.*;
import com.gmail.romkatsis.healthhubserver.services.ClinicService;
import com.gmail.romkatsis.healthhubserver.services.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/clinics")
public class ClinicController {

    private final ClinicService clinicService;

    private final ReviewService reviewService;

    @Autowired
    public ClinicController(ClinicService clinicService, ReviewService reviewService) {
        this.clinicService = clinicService;
        this.reviewService = reviewService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ClinicsSearchResponse findClinics(@Valid ClinicsSearchRequest searchRequest) {
        return clinicService.findClinicsBySearchRequest(searchRequest);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TokensResponse createClinic(@RequestBody @Valid ClinicInfoRequest clinicInfo,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        return clinicService.addClinic(clinicInfo, Integer.parseInt(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClinicInfoResponse getClinicInfo(@PathVariable int id) {
        return clinicService.getClinicInfoById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@clinicService.isAdministratorBelongToClinic(new Integer(principal.username), #id)")
    public ClinicInfoResponse editClinicInfo(@PathVariable int id,
                                             @RequestBody @Valid ClinicInfoRequest clinicInfo) {
        return clinicService.editClinicInfo(id, clinicInfo);
    }

    @PutMapping("/{id}/specialisations")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@clinicService.isAdministratorBelongToClinic(new Integer(principal.username), #id)")
    public ClinicInfoResponse editClinicSpecialisations(@PathVariable int id,
                                                        @RequestBody @Valid SpecialisationsRequest specialisations) {
        return clinicService.editClinicSpecialisations(id, specialisations);
    }

    @PutMapping("/{id}/working-days")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@clinicService.isAdministratorBelongToClinic(new Integer(principal.username), #id)")
    public ClinicInfoResponse editClinicWorkingDays(@PathVariable int id,
                                                    @RequestBody @Valid WorkingDaysRequest workingDays) {
        return clinicService.editClinicWorkingDays(id, workingDays);
    }

    @PutMapping("/{id}/amenities")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@clinicService.isAdministratorBelongToClinic(new Integer(principal.username), #id)")
    public ClinicInfoResponse editClinicAmenities(@PathVariable int id,
                                                  @RequestBody @Valid AmenitiesRequest amenities) {
        return clinicService.editClinicAmenities(id, amenities);
    }

    @PostMapping("/{id}/contacts")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@clinicService.isAdministratorBelongToClinic(new Integer(principal.username), #id)")
    public ClinicInfoResponse addClinicContact(@PathVariable int id,
                                               @RequestBody @Valid ContactRequest contact) {
        return clinicService.addClinicContact(id, contact);
    }

    @DeleteMapping("/{id}/contacts")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@clinicService.isAdministratorBelongToClinic(new Integer(principal.username), #id)")
    public ClinicInfoResponse deleteClinicContact(@PathVariable int id,
                                                  @RequestParam @Valid int contactId) {
        return clinicService.removeClinicContact(id, contactId);
    }

    @GetMapping("/{id}/secret-code")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@clinicService.isAdministratorBelongToClinic(new Integer(principal.username), #id)")
    public SecretCodeResponse getClinicSecretCode(@PathVariable int id) {
        return clinicService.getClinicSecretCode(id);
    }

    @GetMapping("/{id}/doctors")
    @ResponseStatus(HttpStatus.OK)
    public Set<DoctorInfoShortResponse> getClinicDoctors(@PathVariable int id) {
        return clinicService.getDoctorsByClinicId(id);
    }

    @PostMapping("/{id}/doctors/{doctorId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("#doctorId == new Integer(principal.username)")
    public Set<DoctorInfoShortResponse> addDoctorToClinic(@PathVariable int id,
                                                          @PathVariable int doctorId,
                                                          @RequestBody @Valid TokenRequest request) {
        return clinicService.addDoctor(id, doctorId, request);
    }

    @DeleteMapping("/{id}/doctors/{doctorId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("(#doctorId == new Integer(principal.username)) or " +
            "@clinicService.isAdministratorBelongToClinic(new Integer(principal.username), #id)")
    public Set<DoctorInfoShortResponse> deleteDoctorFromClinic(@PathVariable int id,
                                                               @PathVariable int doctorId) {
        return clinicService.removeDoctor(id, doctorId);
    }


    @PostMapping("/{id}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public Set<ReviewDto> addClinicReview(@AuthenticationPrincipal UserDetails userDetails,
                                          @PathVariable int id,
                                          @RequestBody @Valid ReviewRequest request) {
        return reviewService.addClinicReview(
                Integer.parseInt(userDetails.getUsername()),
                id,
                request);
    }

    @GetMapping("/{id}/reviews")
    @ResponseStatus(HttpStatus.OK)
    public Set<ReviewDto> getClinicReviews(@PathVariable int id) {
        return reviewService.getClinicReviews(id);
    }

    @GetMapping("/specialisations")
    @ResponseStatus(HttpStatus.OK)
    public Set<SpecialisationDto> getClinicsSpecialisationsList() {
        return clinicService.getClinicsSpecialisations();
    }

    @GetMapping("/amenities")
    @ResponseStatus(HttpStatus.OK)
    public Set<ClinicAmenityDto> getClinicsAmenitiesList() {
        return clinicService.getClinicsAmenities();
    }

}
