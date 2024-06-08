package com.gmail.romkatsis.healthhubserver.services;

import com.gmail.romkatsis.healthhubserver.dtos.requests.*;
import com.gmail.romkatsis.healthhubserver.dtos.responses.ClinicInfoResponse;
import com.gmail.romkatsis.healthhubserver.dtos.responses.SecretCodeResponse;
import com.gmail.romkatsis.healthhubserver.dtos.responses.TokensResponse;
import com.gmail.romkatsis.healthhubserver.enums.Role;
import com.gmail.romkatsis.healthhubserver.exceptions.ResourceNotFoundException;
import com.gmail.romkatsis.healthhubserver.exceptions.UserResourceLimitExceededException;
import com.gmail.romkatsis.healthhubserver.models.*;
import com.gmail.romkatsis.healthhubserver.repositories.ClinicAmenityRepository;
import com.gmail.romkatsis.healthhubserver.repositories.ClinicRepository;
import com.gmail.romkatsis.healthhubserver.repositories.ClinicSpecialisationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional(readOnly = true)
public class ClinicService {

    private final ClinicRepository clinicRepository;

    private final ModelMapper modelMapper;

    private final UserService userService;

    private final DoctorsDetailsService doctorsDetailsService;

    private final AuthenticationService authenticationService;

    private final ClinicSpecialisationRepository specialisationRepository;

    private final ClinicAmenityRepository amenityRepository;

    @Autowired
    public ClinicService(ClinicRepository clinicRepository, ModelMapper modelMapper, UserService userService, DoctorsDetailsService doctorsDetailsService, AuthenticationService authenticationService, ClinicSpecialisationRepository specialisationRepository, ClinicAmenityRepository amenityRepository) {
        this.clinicRepository = clinicRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.doctorsDetailsService = doctorsDetailsService;
        this.authenticationService = authenticationService;
        this.specialisationRepository = specialisationRepository;
        this.amenityRepository = amenityRepository;
    }

    @Transactional
    public TokensResponse addClinic(ClinicInfoRequest clinicInfo, int userId) {
        User user = userService.findUserById(userId);
        DoctorsDetails doctorsDetails = user.getDoctorsDetails();
        if (doctorsDetails.getClinic() != null) {
            throw new UserResourceLimitExceededException("User already belongs to a clinic and cannot create his own");
        }

        Clinic clinic = modelMapper.map(clinicInfo, Clinic.class);
        clinic.addDoctor(doctorsDetails);
        clinic.setAdmin(doctorsDetails);
        clinic.generateSecretCode();
        clinicRepository.save(clinic);

        user.addRole(Role.ROLE_CLINIC_ADMINISTRATOR);
        return authenticationService.generateTokensByUser(user);
    }

    public Clinic getClinicById(int id) {
        return clinicRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Clinic by id %s not found".formatted(id)));
    }

    @Transactional
    public ClinicInfoResponse editClinicInfo(int clinicId, ClinicInfoRequest clinicInfo) {
        Clinic clinic = getClinicById(clinicId);
        modelMapper.map(clinicInfo, clinic);
        return convertClinicToClinicInfoResponse(clinic);
    }

    public ClinicInfoResponse getClinicInfoById(int id) {
        Clinic clinic = getClinicById(id);
        return convertClinicToClinicInfoResponse(clinic);
    }

    @Transactional
    public ClinicInfoResponse editClinicSpecialisations(int clinicId, SpecialisationsRequest request) {
        Set<ClinicSpecialisation> specialisations =
                specialisationRepository.findAllByIdIn(request.getSpecialisations());

        Clinic clinic = getClinicById(clinicId);
        clinic.assignSpecialisations(specialisations);
        return convertClinicToClinicInfoResponse(clinic);
    }

    @Transactional
    public ClinicInfoResponse editClinicWorkingDays(int clinicId, WorkingDaysRequest request) {
         Set<WorkingDay> workingDays = Set.of(modelMapper.map(request.getDays(), WorkingDay[].class));
         Clinic clinic = getClinicById(clinicId);
         clinic.setWorkingDays(workingDays);
         return convertClinicToClinicInfoResponse(clinic);
    }

    @Transactional
    public ClinicInfoResponse editClinicAmenities(int clinicId, AmenitiesRequest request) {
        Set<ClinicAmenity> amenities = amenityRepository.findAllByIdIn(request.getAmenities());
        Clinic clinic = getClinicById(clinicId);
        clinic.assignAmenities(amenities);
        return convertClinicToClinicInfoResponse(clinic);
    }

    @Transactional
    public ClinicInfoResponse addClinicContact(int clinicId, ContactRequest contactRequest) {
        ClinicContact contact = modelMapper.map(contactRequest, ClinicContact.class);
        Clinic clinic = getClinicById(clinicId);
        clinic.addContact(contact);
        clinicRepository.saveAndFlush(clinic);
        return convertClinicToClinicInfoResponse(clinic);
    }

    @Transactional
    public ClinicInfoResponse removeClinicContact(int clinicId, int contactId) {
        Clinic clinic = getClinicById(clinicId);
        clinic.removeContactById(contactId);
        return convertClinicToClinicInfoResponse(clinic);
    }

    public SecretCodeResponse getClinicSecretCode(int clinicId) {
        Clinic clinic = getClinicById(clinicId);
        return new SecretCodeResponse(clinic.getSecretCode());
    }

    @Transactional
    public Set<ClinicReview> addClinicReview(int clinicId, int userId, ReviewRequest request) {
        Clinic clinic = getClinicById(clinicId);
        User user = userService.findUserById(userId);
        ClinicReview doctorReview = modelMapper.map(request, ClinicReview.class);
        clinic.addReview(doctorReview, user);
        clinicRepository.saveAndFlush(clinic);
        return clinic.getReviews();
    }

    private ClinicInfoResponse convertClinicToClinicInfoResponse(Clinic clinic) {
        return modelMapper.map(clinic, ClinicInfoResponse.class);
    }
}
