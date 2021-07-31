package com.beb.coffeeshop.service.impl;

import java.util.Optional;

import com.beb.coffeeshop.exception.ServiceException;
import com.beb.coffeeshop.exception.UsernameExistsException;
import com.beb.coffeeshop.model.Role;
import com.beb.coffeeshop.model.User;
import com.beb.coffeeshop.repository.UserRepository;
import com.beb.coffeeshop.service.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(String userName, String password, Role role) throws UsernameExistsException, ServiceException {
        Optional<User> userRecord = userRepository.findByUsername(userName);
        if (userRecord.isPresent())
            throw new UsernameExistsException(String.format("Username : %s aslready exists", userName));
        User user = new User();
        user.setUsername(userName);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new ServiceException("Error while creating user.", e);
        }
    }
}
