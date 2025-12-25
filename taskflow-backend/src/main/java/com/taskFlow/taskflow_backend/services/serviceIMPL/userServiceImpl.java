package com.taskFlow.taskflow_backend.services.serviceIMPL;

import com.taskFlow.taskflow_backend.dto.userDTO;
import com.taskFlow.taskflow_backend.model.Entity.User;
import com.taskFlow.taskflow_backend.respository.userRepositoty;
import com.taskFlow.taskflow_backend.services.userService;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        // JWTconfig jwtconfig = new JWTconfig();
        // String token = jwtconfig.generateToken(user.getEmail());
        userRepository.save(user);

        return convertToDTO(user);
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
        return convertToDTO(user);
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
        return convertToDTO(user);
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
        LocalDateTime now = LocalDateTime.now();
        user.setUpdatedAt(now);

        userRepository.save(user);

        return convertToDTO(user);
    }

    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        userRepository.delete(user);
    }

    public List<userDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    userDTO convertToDTO(User user) {
        userDTO userDTO = new userDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setName(user.getName());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setUserRole(user.getUserRole());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        // if (user.getAssignedTasks() != null) {
        // // Convert assigned tasks to DTOs if necessary
        // // userDTO.setAssignedTasks( ... );
        // List<taskDTO.TaskSummaryDTO> taskDTOs = user.getAssignedTasks().stream()
        // .map(task -> {
        // taskDTO.TaskSummaryDTO taskSummaryDTO = new taskDTO.TaskSummaryDTO();
        // taskSummaryDTO.setTaskId(task.getTaskId());
        // taskSummaryDTO.setTitle(task.getTitle());
        // taskSummaryDTO.setStatus(task.getStatus());
        // return taskSummaryDTO;
        // })
        // .collect(Collectors.toList());
        // }
        return userDTO;
    }

}