package com.gmail.romkatsis.healthhubserver.services;

import com.gmail.romkatsis.healthhubserver.exceptions.EmailAlreadyRegisteredException;
import com.gmail.romkatsis.healthhubserver.exceptions.ResourceNotFoundException;
import com.gmail.romkatsis.healthhubserver.models.DoctorsDetails;
import com.gmail.romkatsis.healthhubserver.models.User;
import com.gmail.romkatsis.healthhubserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final DoctorsDetailsService doctorsDetailsService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, DoctorsDetailsService doctorsDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.doctorsDetailsService = doctorsDetailsService;
    }

    public User findUserById(int id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User by id %s not found".formatted(id)));
    }

    @Transactional
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new EmailAlreadyRegisteredException("Email %s already registered".formatted(user.getEmail()));
        }
    }

    @Transactional
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void addDoctorToSaved(int userId, int doctorId) {
        User user = findUserById(userId);
        DoctorsDetails doctorsDetails = doctorsDetailsService.findDoctorsDetailsById(doctorId);
        user.saveDoctor(doctorsDetails);
    }

    @Transactional
    public void removeDoctorFromSaved(int userId, int doctorId) {
        User user = findUserById(userId);
        DoctorsDetails doctorsDetails = doctorsDetailsService.findDoctorsDetailsById(doctorId);
        user.removeDoctorFromSaved(doctorsDetails);
    }

    public Set<DoctorsDetails> getSavedDoctorsByUserId(int userId) {
        User user = findUserById(userId);
        return user.getSavedDoctors();
    }
}
