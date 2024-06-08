package com.gmail.romkatsis.healthhubserver.services;

import com.gmail.romkatsis.healthhubserver.dtos.responses.ClinicInfoShortResponse;
import com.gmail.romkatsis.healthhubserver.dtos.responses.DoctorsDetailsInfoShortResponse;
import com.gmail.romkatsis.healthhubserver.models.Clinic;
import com.gmail.romkatsis.healthhubserver.models.DoctorsDetails;
import com.gmail.romkatsis.healthhubserver.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class SavedDoctorsAndClinicsService {

    private final UserService userService;

    private final ClinicService clinicService;

    private final DoctorsDetailsService doctorsDetailsService;

    private final ModelMapper modelMapper;

    public SavedDoctorsAndClinicsService(UserService userService, ClinicService clinicService, DoctorsDetailsService doctorsDetailsService, ModelMapper modelMapper) {
        this.userService = userService;
        this.clinicService = clinicService;
        this.doctorsDetailsService = doctorsDetailsService;
        this.modelMapper = modelMapper;
    }

    public Set<DoctorsDetailsInfoShortResponse> getSavedDoctorsByUserId(int userId) {
        User user = userService.findUserById(userId);
        return convertDoctorsDetailsToDoctorsDetailsShortResponse(user.getSavedDoctors());
    }

    @Transactional
    public Set<DoctorsDetailsInfoShortResponse> saveDoctor(int userId, int doctorId) {
        User user = userService.findUserById(userId);
        DoctorsDetails doctor = doctorsDetailsService.findDoctorsDetailsById(doctorId);
        user.saveDoctor(doctor);
        return convertDoctorsDetailsToDoctorsDetailsShortResponse(user.getSavedDoctors());
    }

    @Transactional
    public Set<DoctorsDetailsInfoShortResponse> deleteDoctor(int userId, int doctorId) {
        User user = userService.findUserById(userId);
        DoctorsDetails doctor = doctorsDetailsService.findDoctorsDetailsById(doctorId);
        user.removeDoctorFromSaved(doctor);
        return convertDoctorsDetailsToDoctorsDetailsShortResponse(user.getSavedDoctors());
    }

    public Set<ClinicInfoShortResponse> getSavedClinicsByUserId(int userId) {
        User user = userService.findUserById(userId);
        return convertClinicToClinicInfoShortResponse(user.getSavedClinics());
    }

    @Transactional
    public Set<ClinicInfoShortResponse> saveClinic(int userId, int clinicId) {
        User user = userService.findUserById(userId);
        Clinic clinic = clinicService.getClinicById(clinicId);
        user.saveClinic(clinic);
        return convertClinicToClinicInfoShortResponse(user.getSavedClinics());
    }

    @Transactional
    public Set<ClinicInfoShortResponse> deleteClinic(int userId, int clinicId) {
        User user = userService.findUserById(userId);
        Clinic clinic = clinicService.getClinicById(clinicId);
        user.removeClinicFromSaved(clinic);
        return convertClinicToClinicInfoShortResponse(user.getSavedClinics());
    }

    private Set<ClinicInfoShortResponse> convertClinicToClinicInfoShortResponse(Set<Clinic> clinics) {
        return clinics.stream()
                .map(clinic -> modelMapper.map(clinic, ClinicInfoShortResponse.class))
                .collect(Collectors.toSet());
    }

    private Set<DoctorsDetailsInfoShortResponse> convertDoctorsDetailsToDoctorsDetailsShortResponse(Set<DoctorsDetails> doctorsDetails) {
        modelMapper.typeMap(DoctorsDetails.class, DoctorsDetailsInfoShortResponse.class)
                .addMapping(details -> details.getUser().getFirstName(),
                        DoctorsDetailsInfoShortResponse::setFirstName)
                .addMapping(details -> details.getUser().getLastName(),
                        DoctorsDetailsInfoShortResponse::setLastName);
        return doctorsDetails.stream()
                .map(d -> modelMapper.map(d, DoctorsDetailsInfoShortResponse.class))
                .collect(Collectors.toSet());
    }
}
