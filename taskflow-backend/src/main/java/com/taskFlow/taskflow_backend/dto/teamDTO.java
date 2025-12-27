package com.taskFlow.taskflow_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * All Team-related DTOs grouped in one file.
 * Clean, backend-correct, interview-friendly.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class teamDTO {

    private UUID teamId;

    private String teamName;

    private userDTO createdBy;

    private List<taskDTO> task;

    // ===============================
    // 1️⃣ TEAM CREATE REQUEST DTO
    // Used for POST /teams
    // ===============================
    @Data
    public static class CreateTeamRequestDTO {

        @NotBlank(message = "Team name cannot be blank")
        private String teamName;
    }

    // ===============================
    // 2️⃣ TEAM RESPONSE DTO
    // Used for GET /teams/{id}
    // ===============================
    @Data
    public static class TeamResponseDTO {

        private UUID teamId;
        private String teamName;

        // Creator info (flattened)
        private UUID createdByUserId;
        private String createdByUserName;

        // Tasks (summary only, not full entity)
        private List<taskDTO.TaskSummaryDTO> tasks;
    }

    // ===============================
    // 3️⃣ TEAM SUMMARY DTO
    // Used in lists: GET /teams
    // ===============================
    @Data
    public static class TeamSummaryDTO {

        private UUID teamId;
        private String teamName;
        private int totalTasks;
    }

    // ===============================
    // 4️⃣ ADD USER TO TEAM REQUEST DTO
    // Used for POST /teams/{teamId}/members
    // ===============================
    @Data
    public static class AddUserToTeamRequestDTO {

        @NotNull
        private UUID userId;
    }
}
