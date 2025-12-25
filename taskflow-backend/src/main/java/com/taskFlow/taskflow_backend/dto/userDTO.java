package com.taskFlow.taskflow_backend.dto;

import com.taskFlow.taskflow_backend.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This file contains all User-related DTOs.
 * Grouped intentionally to keep learning simple and organized.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class userDTO {
    private UUID userId;
    private String name;
    private String email;
    private String password;
    private UserRole userRole;
    private List<taskDTO.TaskSummaryDTO> assignedTasks = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ===============================
    // 1️⃣ USER RESPONSE DTO
    // Used for GET /users/me, GET /users/{id}
    // ===============================
    @Data
    @Getter
    @Setter
    public static class UserResponseDTO {
        private UUID userId;
        private String name;
        private String email;
        private UserRole userRole;
        private LocalDateTime createdAt;
    }

    // ===============================
    // 2️⃣ USER CREATE (REGISTER) REQUEST DTO
    // Used for POST /auth/register
    // ===============================
    @Data
    public static class UserCreateRequestDTO {

        @NotBlank(message = "Username cannot be blank")
        @Size(min = 3, max = 15)
        private String name;

        @Email
        @NotNull
        @NotBlank(message = "Email cannot be blank")
        private String email;

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 4, max = 15)
        private String password;
    }

    // ===============================
    // 3️⃣ USER LOGIN REQUEST DTO
    // Used for POST /auth/login
    // ===============================
    @Data
    public static class UserLoginRequestDTO {

        @Email
        @NotBlank
        private String email;

        @NotBlank
        private String password;
    }

    // ===============================
    // 4️⃣ USER SUMMARY DTO
    // Used inside Task, Team, Notification responses
    // ===============================
    @Data
    public static class UserSummaryDTO {
        private UUID userId;
        private String name;
        private String email;
    }
}
