package com.gmail.romkatsis.healthhubserver.services;

import com.gmail.romkatsis.healthhubserver.enums.Role;
import com.gmail.romkatsis.healthhubserver.exceptions.ResourceNotFoundException;
import com.gmail.romkatsis.healthhubserver.models.DoctorsDetails;
import com.gmail.romkatsis.healthhubserver.models.User;
import com.gmail.romkatsis.healthhubserver.repositories.DoctorsDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DoctorsDetailsService {

    private final DoctorsDetailsRepository doctorsDetailsRepository;

    public DoctorsDetailsService(DoctorsDetailsRepository doctorsDetailsRepository) {
        this.doctorsDetailsRepository = doctorsDetailsRepository;
    }

    @Transactional
    public void addDoctorDetails(DoctorsDetails doctorsDetails, User user) {
        user.addDoctorsDetails(doctorsDetails);
        user.addRole(Role.ROLE_DOCTOR);
    }

    public DoctorsDetails findDoctorsDetailsById(int id) {
        return doctorsDetailsRepository.findByUserId(id).orElseThrow(() ->
                new ResourceNotFoundException("Doctor by id %s not found".formatted(id)));
    }

    public void editDoctorDetails(DoctorsDetails doctorsDetails) {
        doctorsDetailsRepository.save(doctorsDetails);
    }
}
