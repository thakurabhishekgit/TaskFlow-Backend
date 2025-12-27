package com.taskFlow.taskflow_backend.services;

import java.util.List;
import java.util.UUID;

import com.taskFlow.taskflow_backend.dto.teamMembersDTO;

public interface teamMembersService {

    teamMembersDTO.TeamMemberResponseDTO addMember(
            UUID teamId,
            UUID adminUserId,
            teamMembersDTO.AddTeamMemberRequestDTO request);

    List<teamMembersDTO.TeamMemberResponseDTO> getAllMembers(UUID teamId);

    void removeMember(UUID teamId, UUID adminUserId, UUID userId);
}
