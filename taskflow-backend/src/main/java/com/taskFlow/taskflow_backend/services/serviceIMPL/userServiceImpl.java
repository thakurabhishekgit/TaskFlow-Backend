package com.taskFlow.taskflow_backend.services.serviceIMPL;

import com.taskFlow.taskflow_backend.dto.taskDTO;
import com.taskFlow.taskflow_backend.dto.userDTO;
import com.taskFlow.taskflow_backend.model.Entity.User;
import com.taskFlow.taskflow_backend.respository.userRepositoty;
import com.taskFlow.taskflow_backend.services.userService;

import org.springframework.cache.annotation.Cacheable;
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
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUserRole(userDTO.getUserRole());
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

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
    @Cacheable(value = "users", key = "#userId", unless = "#result == null")

    public userDTO getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        user = userRepository.findUserWithTasks(userId)
                .orElseThrow(() -> new RuntimeException("User not found with task"));
        userDTO userDTO = new userDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setUserRole(user.getUserRole());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        userDTO.setPassword(user.getPassword());
        return convertToDTOForOneTime(user);
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

    @Transactional
    public List<userDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public userDTO convertToDTOForOneTime(User user) {

        userDTO dto = new userDTO();
        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());
        dto.setUserRole(user.getUserRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        if (user.getAssignedTasks() != null) {
            List<taskDTO.TaskSummaryDTO> taskSummaries = user.getAssignedTasks().stream()
                    .map(task -> {
                        taskDTO.TaskSummaryDTO t = new taskDTO.TaskSummaryDTO();
                        t.setTaskId(task.getTaskId());
                        t.setTaskTitle(task.getTaskTitle());
                        t.setStatus(task.getStatus());
                        t.setPriority(task.getPriority());
                        return t;
                    })
                    .toList();

            dto.setAssignedTasks(taskSummaries);
        }

        return dto;
    }

    public userDTO convertToDTO(User user) {
        userDTO dto = new userDTO();
        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());
        dto.setUserRole(user.getUserRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        return dto;
    }
}