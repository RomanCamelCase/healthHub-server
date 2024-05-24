package com.gmail.romkatsis.healthhubserver.services;

import com.gmail.romkatsis.healthhubserver.exceptions.UserNotFoundException;
import com.gmail.romkatsis.healthhubserver.models.User;
import com.gmail.romkatsis.healthhubserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
