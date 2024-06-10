package com.gmail.romkatsis.healthhubserver.services;

import com.gmail.romkatsis.healthhubserver.dtos.requests.*;
import com.gmail.romkatsis.healthhubserver.dtos.responses.*;
import com.gmail.romkatsis.healthhubserver.enums.Role;
import com.gmail.romkatsis.healthhubserver.exceptions.DataIntegrityException;
import com.gmail.romkatsis.healthhubserver.exceptions.InvalidClinicSecretCodeException;
import com.gmail.romkatsis.healthhubserver.exceptions.ResourceNotFoundException;
import com.gmail.romkatsis.healthhubserver.exceptions.UserResourceLimitExceededException;
import com.gmail.romkatsis.healthhubserver.models.*;
import com.gmail.romkatsis.healthhubserver.repositories.ClinicAmenityRepository;
import com.gmail.romkatsis.healthhubserver.repositories.ClinicRepository;
import com.gmail.romkatsis.healthhubserver.repositories.ClinicSpecialisationRepository;
import com.gmail.romkatsis.healthhubserver.specifications.ClinicSpecification;
import com.gmail.romkatsis.healthhubserver.utils.GoogleMapsApiUtils;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    private final GoogleMapsApiUtils googleMapsApiUtils;

    private final EntityManager entityManager;

    @Autowired
    public ClinicService(ClinicRepository clinicRepository, ModelMapper modelMapper, UserService userService, DoctorsDetailsService doctorsDetailsService, AuthenticationService authenticationService, ClinicSpecialisationRepository specialisationRepository, ClinicAmenityRepository amenityRepository, GoogleMapsApiUtils googleMapsApiUtils, EntityManager entityManager) {
        this.clinicRepository = clinicRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.doctorsDetailsService = doctorsDetailsService;
        this.authenticationService = authenticationService;
        this.specialisationRepository = specialisationRepository;
        this.amenityRepository = amenityRepository;
        this.googleMapsApiUtils = googleMapsApiUtils;
        this.entityManager = entityManager;
    }

    public ClinicsSearchResponse findClinicsBySearchRequest(ClinicsSearchRequest searchRequest) {
        Pageable pageable = PageRequest.of(searchRequest.getPageNumber(), searchRequest.getPageSize());

        Specification<Clinic> clinicSpecification = ClinicSpecification.findBy(
                searchRequest.getCity(),
                specialisationRepository.findById(searchRequest.getSpecialisationId()).orElse(null),
                searchRequest.getIsPrivate(),
                amenityRepository.findAllByIdIn(searchRequest.getAmenitiesIds()),
                searchRequest.getMinRating(),
                searchRequest.getSortBy()
        );

        Page<Clinic> clinics = clinicRepository.findAll(clinicSpecification, pageable);
        return new ClinicsSearchResponse(clinics.getTotalPages(), (int) clinics.getTotalElements(),
                convertClinicToClinicInfoShortResponse(new LinkedHashSet<>(clinics.getContent())));
    }

    @Transactional
    public TokensResponse addClinic(ClinicInfoRequest clinicInfo, int userId) {
        User user = userService.findUserById(userId);
        DoctorsDetails doctorsDetails = user.getDoctorsDetails();
        if (doctorsDetails.getClinic() != null) {
            throw new UserResourceLimitExceededException("User already belongs to a clinic and cannot create new one");
        }

        Clinic clinic = modelMapper.map(clinicInfo, Clinic.class);
        clinic.addDoctor(doctorsDetails);
        clinic.setAdmin(doctorsDetails);
        clinic.setGoogleMapsPlaceId(findGoogleMapsPlaceId(clinicInfo));
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
        if (!clinic.getCity().equals(clinicInfo.getCity()) ||
                !clinic.getAddress().equals(clinicInfo.getAddress())) {
            clinic.setGoogleMapsPlaceId(findGoogleMapsPlaceId(clinicInfo));
        }

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

    public Set<DoctorInfoShortResponse> getDoctorsByClinicId(int clinicId) {
        Clinic clinic = getClinicById(clinicId);
        return convertDoctorsDetailsToDoctorInfoShortResponse(clinic.getDoctors());
    }

    @Transactional
    public Set<DoctorInfoShortResponse> addDoctor(int clinicId, ClinicNewDoctorRequest request) {
        Clinic clinic = getClinicById(clinicId);

        if (!clinic.getSecretCode().equals(request.getSecretCode())) {
            throw new InvalidClinicSecretCodeException(
                    "Your secret code is incorrect. Please contact the clinic administrator.");
        }

        DoctorsDetails doctor = doctorsDetailsService.findDoctorsDetailsById(request.getDoctorId());
        if (doctor.getClinic() != null) {
            throw new UserResourceLimitExceededException(
                    "Doctor already belongs to clinic and cannot join this");
        }

        clinic.addDoctor(doctor);
        return convertDoctorsDetailsToDoctorInfoShortResponse(clinic.getDoctors());
    }

    @Transactional
    public Set<DoctorInfoShortResponse> removeDoctor(int clinicId, int doctorId) {
        Clinic clinic = getClinicById(clinicId);
        if (clinic.getAdmin().getUserId() == doctorId) {
            throw new DataIntegrityException("Clinic can not exists without an admin");
        }
        DoctorsDetails doctor = doctorsDetailsService.findDoctorsDetailsById(doctorId);
        clinic.removeDoctor(doctor);
        return convertDoctorsDetailsToDoctorInfoShortResponse(clinic.getDoctors());
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
        entityManager.refresh(clinic);
        return clinic.getReviews();
    }

    private String findGoogleMapsPlaceId(ClinicInfoRequest request) {
        return googleMapsApiUtils.getPlaceIdByAddress("ua", request.getCity(), request.getAddress());
    }

    private ClinicInfoResponse convertClinicToClinicInfoResponse(Clinic clinic) {
        return modelMapper.map(clinic, ClinicInfoResponse.class);
    }

    private LinkedHashSet<ClinicInfoShortResponse> convertClinicToClinicInfoShortResponse(LinkedHashSet<Clinic> clinics) {
        return clinics.stream()
                .map(clinic -> modelMapper.map(clinic, ClinicInfoShortResponse.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Set<DoctorInfoShortResponse> convertDoctorsDetailsToDoctorInfoShortResponse(Set<DoctorsDetails> doctorsDetails) {
        return doctorsDetails.stream()
                .map(d -> modelMapper.map(d, DoctorInfoShortResponse.class))
                .collect(Collectors.toSet());
    }
}
