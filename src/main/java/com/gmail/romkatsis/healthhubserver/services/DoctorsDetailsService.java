package com.gmail.romkatsis.healthhubserver.services;

import com.gmail.romkatsis.healthhubserver.dtos.embedded.SpecialisationDto;
import com.gmail.romkatsis.healthhubserver.dtos.requests.*;
import com.gmail.romkatsis.healthhubserver.dtos.responses.DoctorInfoResponse;
import com.gmail.romkatsis.healthhubserver.dtos.responses.DoctorInfoShortResponse;
import com.gmail.romkatsis.healthhubserver.dtos.responses.DoctorsSearchResponse;
import com.gmail.romkatsis.healthhubserver.dtos.responses.TokensResponse;
import com.gmail.romkatsis.healthhubserver.enums.Role;
import com.gmail.romkatsis.healthhubserver.exceptions.ResourceNotFoundException;
import com.gmail.romkatsis.healthhubserver.exceptions.UserResourceLimitExceededException;
import com.gmail.romkatsis.healthhubserver.models.*;
import com.gmail.romkatsis.healthhubserver.repositories.DoctorSpecialisationRepository;
import com.gmail.romkatsis.healthhubserver.repositories.DoctorsDetailsRepository;
import com.gmail.romkatsis.healthhubserver.specifications.DoctorSpecification;
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

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DoctorsDetailsService {

    private final DoctorsDetailsRepository doctorsDetailsRepository;

    private final DoctorSpecialisationRepository specialisationRepository;

    private final UserService userService;

    private final AuthenticationService authenticationService;

    private final ModelMapper modelMapper;

    private final GoogleMapsApiUtils googleMapsApiUtils;

    private final EntityManager entityManager;

    @Autowired
    public DoctorsDetailsService(DoctorsDetailsRepository doctorsDetailsRepository,
                                 DoctorSpecialisationRepository specialisationRepository,
                                 UserService userService,
                                 AuthenticationService authenticationService,
                                 ModelMapper modelMapper, GoogleMapsApiUtils googleMapsApiUtils, EntityManager entityManager) {
        this.doctorsDetailsRepository = doctorsDetailsRepository;
        this.specialisationRepository = specialisationRepository;
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.modelMapper = modelMapper;
        this.googleMapsApiUtils = googleMapsApiUtils;
        this.entityManager = entityManager;
    }

    public DoctorsSearchResponse findDoctorsBySearchRequest(DoctorSearchRequest searchRequest) {
        Pageable pageable = PageRequest.of(searchRequest.getPageNumber(), searchRequest.getPageSize());

        Specification<DoctorsDetails> doctorSpecification = DoctorSpecification.findBy(
                searchRequest.getCity(),
                specialisationRepository.findById(searchRequest.getSpecialisationId()).orElse(null),
                searchRequest.getWorkWith(),
                LocalDate.now().minusYears(searchRequest.getWorkExperienceYears()),
                searchRequest.getGender(),
                searchRequest.getMinRating(),
                searchRequest.getSortBy()
        );

        Page<DoctorsDetails> doctorsPage = doctorsDetailsRepository.findAll(doctorSpecification, pageable);
        return new DoctorsSearchResponse(doctorsPage.getTotalPages(), (int) doctorsPage.getTotalElements(),
                convertDoctorsDetailsToDoctorInfoShortResponse(new LinkedHashSet<>(doctorsPage.getContent())));
    }

    public DoctorsDetails findDoctorsDetailsById(int id) {
        return doctorsDetailsRepository.findByUserId(id).orElseThrow(() ->
                new ResourceNotFoundException("Doctor by id %s not found".formatted(id)));
    }

    @Transactional
    public TokensResponse addDoctorDetails(int userId, DoctorInfoRequest request) {
        DoctorsDetails doctorsDetails = modelMapper.map(request, DoctorsDetails.class);
        doctorsDetails.setActive(true);
        doctorsDetails.setGoogleMapsPlaceId(findGoogleMapsPlaceId(request));

        User user = userService.findUserById(userId);
        if (user.getDoctorsDetails() != null) {
            throw new UserResourceLimitExceededException(
                    "User already has doctor account details. Please use methods to edit the information");
        }

        user.addDoctorsDetails(doctorsDetails);
        user.addRole(Role.ROLE_DOCTOR);
        return authenticationService.generateTokensByUser(user);
    }

    public DoctorInfoResponse getDoctorInfoById(int id) {
        DoctorsDetails doctorsDetails = findDoctorsDetailsById(id);
        return convertDoctorDetailsToDoctorInfoResponse(doctorsDetails);
    }

    @Transactional
    public DoctorInfoResponse editDoctorInfo(int doctorId, DoctorInfoRequest request) {
        DoctorsDetails doctorsDetails = findDoctorsDetailsById(doctorId);
        if (!doctorsDetails.getCity().equals(request.getCity()) ||
                !doctorsDetails.getAddress().equals(request.getAddress())) {
            doctorsDetails.setGoogleMapsPlaceId(findGoogleMapsPlaceId(request));
        }

        modelMapper.map(request, doctorsDetails);
        return convertDoctorDetailsToDoctorInfoResponse(doctorsDetails);
    }

    @Transactional
    public DoctorInfoResponse editDoctorSpecialisations(int doctorId, SpecialisationsRequest request) {
        Set<DoctorSpecialisation> specialisationSet =
                specialisationRepository.findAllByIdIn(request.getSpecialisations());
        DoctorsDetails doctorsDetails = findDoctorsDetailsById(doctorId);
        doctorsDetails.assignSpecialisations(specialisationSet);
        return convertDoctorDetailsToDoctorInfoResponse(doctorsDetails);
    }

    @Transactional
    public DoctorInfoResponse editDoctorWorkingDays(int doctorId, WorkingDaysRequest request) {
        Set<WorkingDay> workingDays = Set.of(modelMapper.map(request.getDays(), WorkingDay[].class));
        DoctorsDetails doctorsDetails = findDoctorsDetailsById(doctorId);
        doctorsDetails.setWorkingDays(workingDays);
        return convertDoctorDetailsToDoctorInfoResponse(doctorsDetails);
    }

    @Transactional
    public DoctorInfoResponse addDoctorContact(int doctorId, ContactRequest contactRequest) {
        DoctorContact contact = modelMapper.map(contactRequest, DoctorContact.class);
        DoctorsDetails doctorsDetails = findDoctorsDetailsById(doctorId);
        doctorsDetails.addContact(contact);
        doctorsDetailsRepository.saveAndFlush(doctorsDetails);
        return convertDoctorDetailsToDoctorInfoResponse(doctorsDetails);
    }

    @Transactional
    public DoctorInfoResponse removeDoctorContact(int doctorId, int contactId) {
        DoctorsDetails doctorsDetails = findDoctorsDetailsById(doctorId);
        doctorsDetails.removeContactById(contactId);
        return convertDoctorDetailsToDoctorInfoResponse(doctorsDetails);
    }

    @Transactional
    public DoctorInfoResponse editDoctorStatus(int doctorId, DoctorStatusRequest request) {
        DoctorsDetails doctorsDetails = findDoctorsDetailsById(doctorId);
        doctorsDetails.setActive(request.getActive());
        return convertDoctorDetailsToDoctorInfoResponse(doctorsDetails);
    }

    @Transactional
    public Set<DoctorReview> addDoctorReview(int doctorId, int userId, ReviewRequest request) {
        DoctorsDetails doctorsDetails = findDoctorsDetailsById(doctorId);
        User user = userService.findUserById(userId);
        DoctorReview doctorReview = modelMapper.map(request, DoctorReview.class);
        doctorsDetails.addReview(doctorReview, user);
        doctorsDetailsRepository.saveAndFlush(doctorsDetails);
        entityManager.refresh(doctorsDetails);
        return doctorsDetails.getReviews();
    }

    private String findGoogleMapsPlaceId(DoctorInfoRequest request) {
        return googleMapsApiUtils.getPlaceIdByAddress("ua", request.getCity(), request.getAddress());
    }

    private DoctorInfoResponse convertDoctorDetailsToDoctorInfoResponse(DoctorsDetails doctorsDetails) {
        return modelMapper.map(doctorsDetails, DoctorInfoResponse.class);
    }

    private LinkedHashSet<DoctorInfoShortResponse> convertDoctorsDetailsToDoctorInfoShortResponse(LinkedHashSet<DoctorsDetails> doctorsDetails) {
        return doctorsDetails.stream()
                .map(d -> modelMapper.map(d, DoctorInfoShortResponse.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<SpecialisationDto> getDoctorsSpecialisations() {
        return specialisationRepository.findAll().stream()
                .map(specialisation -> modelMapper.map(specialisation, SpecialisationDto.class))
                .collect(Collectors.toSet());
    }
}
