package com.gmail.romkatsis.healthhubserver.services;

import com.gmail.romkatsis.healthhubserver.enums.Role;
import com.gmail.romkatsis.healthhubserver.exceptions.EmailAlreadyRegisteredException;
import com.gmail.romkatsis.healthhubserver.exceptions.UserNotFoundException;
import com.gmail.romkatsis.healthhubserver.models.User;
import com.gmail.romkatsis.healthhubserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserById(int id) {
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("User by id %s not found".formatted(id)));
    }

    @Transactional
    public void saveUser(User user) {
        user.setRegistrationDate(LocalDate.now());
        user.setRoles(Collections.singleton(Role.ROLE_CUSTOMER));

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new EmailAlreadyRegisteredException("Email %s already registered".formatted(user.getEmail()));
        }
    }
}
