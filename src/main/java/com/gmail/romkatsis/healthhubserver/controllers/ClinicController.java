package com.gmail.romkatsis.healthhubserver.controllers;

import com.gmail.romkatsis.healthhubserver.dtos.requests.*;
import com.gmail.romkatsis.healthhubserver.dtos.responses.ClinicInfoResponse;
import com.gmail.romkatsis.healthhubserver.dtos.responses.SecretCodeResponse;
import com.gmail.romkatsis.healthhubserver.dtos.responses.TokensResponse;
import com.gmail.romkatsis.healthhubserver.services.ClinicService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clinics")
public class ClinicController {

    private final ClinicService clinicService;

    @Autowired
    public ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
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
    //    @PreAuthorize("")
    public ClinicInfoResponse editClinicInfo(@PathVariable int id,
                                             @RequestBody @Valid ClinicInfoRequest clinicInfo) {
        return clinicService.editClinicInfo(id, clinicInfo);
    }

    @PutMapping("/{id}/specialisations")
    @ResponseStatus(HttpStatus.OK)
    //    @PreAuthorize("")
    public ClinicInfoResponse editClinicSpecialisations(@PathVariable int id,
                                                        @RequestBody @Valid SpecialisationsRequest specialisations) {
        return clinicService.editClinicSpecialisations(id, specialisations);
    }

    @PutMapping("/{id}/working-days")
    @ResponseStatus(HttpStatus.OK)
    //    @PreAuthorize("")
    public ClinicInfoResponse editClinicWorkingDays(@PathVariable int id,
                                                    @RequestBody @Valid WorkingDaysRequest workingDays) {
        return clinicService.editClinicWorkingDays(id, workingDays);
    }

    @PutMapping("/{id}/amenities")
    @ResponseStatus(HttpStatus.OK)
    //    @PreAuthorize("")
    public ClinicInfoResponse editClinicAmenities(@PathVariable int id,
                                                  @RequestBody @Valid AmenitiesRequest amenities) {
        return clinicService.editClinicAmenities(id, amenities);
    }

    @PostMapping("/{id}/contacts")
    @ResponseStatus(HttpStatus.CREATED)
    //    @PreAuthorize("")
    public ClinicInfoResponse addClinicContact(@PathVariable int id,
                                               @RequestBody @Valid ContactRequest contact) {
        return clinicService.addClinicContact(id, contact);
    }

    @DeleteMapping("/{id}/contacts")
    @ResponseStatus(HttpStatus.OK)
    //    @PreAuthorize("")
    public ClinicInfoResponse deleteClinicContact(@PathVariable int id,
                                                  @RequestParam @Valid int contactId) {
        return clinicService.removeClinicContact(id, contactId);
    }

    @GetMapping("/{id}/secret-code")
    @ResponseStatus(HttpStatus.OK)
    //    @PreAuthorize("")
    public SecretCodeResponse getClinicSecretCode(@PathVariable int id) {
        return clinicService.getClinicSecretCode(id);
    }


}
