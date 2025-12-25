package com.taskFlow.taskflow_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * All Notification-related DTOs grouped in one file.
 * Naming and structure consistent with UserDTOs, TaskDTOs, TeamDTOs.
 */
public class notificationsDTO {

    // ===============================
    // 1️⃣ NOTIFICATION RESPONSE DTO
    // Used for GET /notifications
    // ===============================
    @Data
    public static class NotificationResponseDTO {

        private UUID notificationId;

        private UUID userId;
        private String userName;

        private String message;
        private boolean isRead;
        private LocalDateTime createdAt;
    }

    // ===============================
    // 2️⃣ MARK NOTIFICATION AS READ REQUEST DTO
    // Used for PATCH /notifications/{id}/read
    // ===============================
    @Data
    public static class MarkNotificationReadRequestDTO {

        @NotNull
        private UUID notificationId;
    }

    // ===============================
    // 3️⃣ NOTIFICATION SUMMARY DTO
    // Used in dashboards / headers
    // ===============================
    @Data
    public static class NotificationSummaryDTO {

        private UUID notificationId;
        private String message;
        private boolean isRead;
    }
}
