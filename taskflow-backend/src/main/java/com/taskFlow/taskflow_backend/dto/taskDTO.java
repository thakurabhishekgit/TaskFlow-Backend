package com.taskFlow.taskflow_backend.dto;

import com.taskFlow.taskflow_backend.model.enums.TaskPriority;
import com.taskFlow.taskflow_backend.model.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * All Task-related DTOs grouped in one file
 * for clean backend development and learning.
 */
public class taskDTO {

    // ===============================
    // 1️⃣ TASK CREATE REQUEST DTO
    // Used for POST /tasks
    // ===============================
    @Data
    public static class CreateTaskRequestDTO {

        @NotBlank(message = "Task title cannot be blank")
        @Size(min = 3, max = 15)
        private String taskTitle;

        @NotBlank(message = "Task description cannot be blank")
        @Size(min = 3, max = 150)
        private String taskDescription;

        @NotNull
        private TaskPriority priority;

        @NotNull
        private UUID teamId;

        @NotNull
        private UUID assignedUserId;
    }

    // ===============================
    // 2️⃣ TASK UPDATE STATUS REQUEST DTO
    // Used for PATCH /tasks/{id}/status
    // ===============================
    @Data
    public static class UpdateTaskStatusRequestDTO {

        @NotNull
        private TaskStatus status;
    }

    // ===============================
    // 3️⃣ TASK RESPONSE DTO
    // Used for GET /tasks, POST /tasks response
    // ===============================
    @Data
    public static class TaskResponseDTO {

        private UUID taskId;
        private String taskTitle;
        private String taskDescription;
        private TaskStatus status;
        private TaskPriority priority;

        // Flattened fields (VERY IMPORTANT)
        private UUID assignedUserId;
        private String assignedUserName;

        private UUID teamId;
        private String teamName;

        private UUID createdByUserId;
        private String createdByUserName;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    // ===============================
    // 4️⃣ TASK SUMMARY DTO
    // Used inside Team or Dashboard views
    // ===============================
    @Data
    public static class TaskSummaryDTO {

        private UUID taskId;
        private String taskTitle;
        private TaskStatus status;
        private TaskPriority priority;
    }
}
