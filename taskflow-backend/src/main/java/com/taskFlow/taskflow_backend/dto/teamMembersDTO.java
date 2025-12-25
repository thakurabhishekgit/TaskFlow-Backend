package com.taskFlow.taskflow_backend.dto;

import com.taskFlow.taskflow_backend.model.enums.TeamRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

/**
 * All TeamMember-related DTOs grouped in one file.
 * Naming kept consistent with UserDTOs, TaskDTOs, TeamDTOs.
 */
public class teamMembersDTO {

    // ===============================
    // ADD USER TO TEAM REQUEST DTO
    // Used for POST /teams/{teamId}/members
    // ===============================
    @Data
    public static class AddTeamMemberRequestDTO {

        @NotNull
        private UUID userId;

        @NotNull
        private TeamRole roleInTeam;
    }

    // ===============================
    // TEAM MEMBER RESPONSE DTO
    // Used for GET /teams/{teamId}/members
    // ===============================
    @Data
    public static class TeamMemberResponseDTO {

        private UUID teamId;
        private UUID userId;

        private String userName;
        private String userEmail;

        private TeamRole roleInTeam;
    }

    // ===============================
    // TEAM MEMBER SUMMARY DTO
    // Used inside TeamResponseDTO
    // ===============================
    @Data
    public static class TeamMemberSummaryDTO {

        private UUID userId;
        private String userName;
        private TeamRole roleInTeam;
    }
}
