package com.taskFlow.taskflow_backend.services;

import java.util.List;
import java.util.UUID;

import com.taskFlow.taskflow_backend.dto.teamDTO;

public interface teamService {

        teamDTO.TeamResponseDTO createTeam(
                        teamDTO.CreateTeamRequestDTO request,
                        UUID creatorUserId);

        List<teamDTO.TeamSummaryDTO> getAllTeams();

        teamDTO.TeamResponseDTO getTeamById(UUID teamId);

        teamDTO.TeamResponseDTO addUserToTeam(
                        UUID teamId,
                        UUID adminUserId,
                        teamDTO.AddUserToTeamRequestDTO request);
}
