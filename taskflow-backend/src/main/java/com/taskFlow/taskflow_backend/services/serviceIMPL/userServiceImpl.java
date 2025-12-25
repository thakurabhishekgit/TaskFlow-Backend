package com.taskFlow.taskflow_backend.services.serviceIMPL;

import com.taskFlow.taskflow_backend.dto.userDTO;
import com.taskFlow.taskflow_backend.model.Entity.User;
import com.taskFlow.taskflow_backend.respository.userRepositoty;
import com.taskFlow.taskflow_backend.services.userService;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class userServiceImpl implements userService {

    private final userRepositoty userRepository;

    private final PasswordEncoder passwordEncoder;

    public userServiceImpl(userRepositoty userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public userDTO registerUser(userDTO userDTO) {

        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        // Additional registration logic (e.g., hashing password) can be added here
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUserRole(userDTO.getUserRole());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // JWTconfig jwtconfig = new JWTconfig();
        // String token = jwtconfig.generateToken(user.getEmail());
        userRepository.save(user);

        return userDTO;
    }

    @Override
    @Transactional
    public userDTO loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        userDTO userDTO = new userDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setUserRole(user.getUserRole());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        return userDTO;
    }

    @Override
    @Transactional
    public userDTO getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        userDTO userDTO = new userDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setUserRole(user.getUserRole());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        return userDTO;
    }

    @Override
    @Transactional
    public userDTO updateUser(UUID userId, userDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUserRole(userDTO.getUserRole());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return userDTO;
    }

    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        userRepository.delete(user);
    }

}