package com.motchecker.mot_reminder.service.impl;

import com.motchecker.mot_reminder.dto.request.UserRegisterDTO;
import com.motchecker.mot_reminder.exception.UserAlreadyExistsException;
import com.motchecker.mot_reminder.mapper.UserMapper;
import com.motchecker.mot_reminder.model.User;
import com.motchecker.mot_reminder.repository.UserRepository;
import com.motchecker.mot_reminder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(UserRegisterDTO userRegisterDTO) {
        // Business logic: Check if email already exists
        if (userRepository.findByEmail(userRegisterDTO.getEmail()) != null) {
            logger.warn("Registration failed: Email {} is already in use.", userRegisterDTO.getEmail());
            throw new UserAlreadyExistsException("Email is already in use");
        }

        User user = userMapper.toEntity(userRegisterDTO);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        logger.info("New user registered successfully: {}", user.getEmail());
    }

    @Override
    public User authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);

        // Business logic: Verify credentials
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            logger.info("User logged in successfully: {}", email);
            return user;
        }

        logger.warn("Failed login attempt for email: {}", email);
        return null;
    }
}