package com.gmail.romkatsis.healthhubserver.services;

import com.gmail.romkatsis.healthhubserver.dtos.requests.RegistrationRequest;
import com.gmail.romkatsis.healthhubserver.dtos.requests.UserInfoRequest;
import com.gmail.romkatsis.healthhubserver.dtos.responses.CurrentUserInfoResponse;
import com.gmail.romkatsis.healthhubserver.enums.Role;
import com.gmail.romkatsis.healthhubserver.exceptions.EmailAlreadyRegisteredException;
import com.gmail.romkatsis.healthhubserver.exceptions.ResourceNotFoundException;
import com.gmail.romkatsis.healthhubserver.models.User;
import com.gmail.romkatsis.healthhubserver.repositories.UserRepository;
import org.modelmapper.ModelMapper;
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

    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public User findUserById(int id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User by id %s not found".formatted(id)));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("User by email %s not found".formatted(email)));
    }

    public CurrentUserInfoResponse getUserInfoById(int id) {
        User user = findUserById(id);
        return convertUserToCurrentUserResponse(user);
    }

    @Transactional
    public User saveUser(RegistrationRequest request) {
        User user = modelMapper.map(request, User.class);
        user.addRole(Role.ROLE_CUSTOMER);
        user.setRegistrationDate(LocalDate.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new EmailAlreadyRegisteredException("Email %s already registered".formatted(user.getEmail()));
        }
    }

    @Transactional
    public CurrentUserInfoResponse editUserInfo(int userId, UserInfoRequest userInfoRequest) {
        User user = findUserById(userId);
        modelMapper.map(userInfoRequest, user);
        return convertUserToCurrentUserResponse(user);
    }

    private CurrentUserInfoResponse convertUserToCurrentUserResponse(User user) {
        return modelMapper.map(user, CurrentUserInfoResponse.class);
    }
}
