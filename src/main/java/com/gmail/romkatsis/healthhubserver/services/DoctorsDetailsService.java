package com.gmail.romkatsis.healthhubserver.services;

import com.gmail.romkatsis.healthhubserver.dtos.requests.*;
import com.gmail.romkatsis.healthhubserver.dtos.responses.DoctorInfoResponse;
import com.gmail.romkatsis.healthhubserver.dtos.responses.TokensResponse;
import com.gmail.romkatsis.healthhubserver.enums.Role;
import com.gmail.romkatsis.healthhubserver.exceptions.ResourceNotFoundException;
import com.gmail.romkatsis.healthhubserver.models.*;
import com.gmail.romkatsis.healthhubserver.repositories.DoctorSpecialisationRepository;
import com.gmail.romkatsis.healthhubserver.repositories.DoctorsDetailsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional(readOnly = true)
public class DoctorsDetailsService {

    private final DoctorsDetailsRepository doctorsDetailsRepository;

    private final DoctorSpecialisationRepository specialisationRepository;

    private final UserService userService;

    private final AuthenticationService authenticationService;

    private final ModelMapper modelMapper;

    @Autowired
    public DoctorsDetailsService(DoctorsDetailsRepository doctorsDetailsRepository,
                                 DoctorSpecialisationRepository specialisationRepository,
                                 UserService userService,
                                 AuthenticationService authenticationService,
                                 ModelMapper modelMapper) {
        this.doctorsDetailsRepository = doctorsDetailsRepository;
        this.specialisationRepository = specialisationRepository;
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.modelMapper = modelMapper;
    }

    public DoctorsDetails findDoctorsDetailsById(int id) {
        return doctorsDetailsRepository.findByUserId(id).orElseThrow(() ->
                new ResourceNotFoundException("Doctor by id %s not found".formatted(id)));
    }

    @Transactional
    public TokensResponse addDoctorDetails(int userId, DoctorInfoRequest request) {
        DoctorsDetails doctorsDetails = modelMapper.map(request, DoctorsDetails.class);
        doctorsDetails.setActive(true);

        User user = userService.findUserById(userId);
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
        doctorsDetails.setActive(request.isActive());
        return convertDoctorDetailsToDoctorInfoResponse(doctorsDetails);
    }

    private DoctorInfoResponse convertDoctorDetailsToDoctorInfoResponse(DoctorsDetails doctorsDetails) {
        modelMapper.typeMap(DoctorsDetails.class, DoctorInfoResponse.class)
                .addMapping(doctor -> doctor.getUser().getFirstName(),
                        DoctorInfoResponse::setFirstName)
                .addMapping(doctor -> doctor.getUser().getLastName(),
                        DoctorInfoResponse::setLastName)
                .addMapping(doctor -> doctor.getUser().getGender(),
                        DoctorInfoResponse::setGender)
                .addMapping(doctor -> doctor.getUser().getRegistrationDate(),
                        DoctorInfoResponse::setRegistrationDate);
        return modelMapper.map(doctorsDetails, DoctorInfoResponse.class);
    }
}
