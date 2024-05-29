package com.gmail.romkatsis.healthhubserver.services;

import com.gmail.romkatsis.healthhubserver.enums.Role;
import com.gmail.romkatsis.healthhubserver.exceptions.EmailAlreadyRegisteredException;
import com.gmail.romkatsis.healthhubserver.exceptions.UserNotFoundException;
import com.gmail.romkatsis.healthhubserver.models.User;
import com.gmail.romkatsis.healthhubserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findUserById(int id) {
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("User by id %s not found".formatted(id)));
    }

    @Transactional
    public void saveUser(User user) {
        user.setRegistrationDate(LocalDate.now());
        user.addRole(Role.ROLE_CUSTOMER);
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
}
