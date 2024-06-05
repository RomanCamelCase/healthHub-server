package com.gmail.romkatsis.healthhubserver.services;

import com.gmail.romkatsis.healthhubserver.enums.Role;
import com.gmail.romkatsis.healthhubserver.exceptions.ResourceNotFoundException;
import com.gmail.romkatsis.healthhubserver.models.DoctorsContact;
import com.gmail.romkatsis.healthhubserver.models.DoctorsDetails;
import com.gmail.romkatsis.healthhubserver.models.User;
import com.gmail.romkatsis.healthhubserver.models.WorkingDay;
import com.gmail.romkatsis.healthhubserver.repositories.DoctorsDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.Set;

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

    @Transactional
    public Set<DoctorsContact> addDoctorsContact(int id, DoctorsContact contact) {
        DoctorsDetails doctorsDetails = findDoctorsDetailsById(id);
        doctorsDetails.addContact(contact);
        return doctorsDetails.getContacts();
    }

    @Transactional
    public Set<DoctorsContact> deleteDoctorsContact(int id, int contactId) {
        DoctorsDetails doctorsDetails = findDoctorsDetailsById(id);
        doctorsDetails.deleteContactById(contactId);
        return doctorsDetails.getContacts();
    }

    @Transactional
    public Set<WorkingDay> addWorkingDay(int id, WorkingDay workingDay) {
        DoctorsDetails doctorsDetails = findDoctorsDetailsById(id);
        doctorsDetails.addOrUpdateWorkingDay(workingDay);
        return doctorsDetails.getWorkingDays();
    }

    @Transactional
    public Set<WorkingDay> deleteDoctorsWorkingDay(int id, DayOfWeek dayOfWeek) {
        DoctorsDetails doctorsDetails = findDoctorsDetailsById(id);
        doctorsDetails.removeWorkingDayByDayOfWeek(dayOfWeek);
        return doctorsDetails.getWorkingDays();
    }
}
