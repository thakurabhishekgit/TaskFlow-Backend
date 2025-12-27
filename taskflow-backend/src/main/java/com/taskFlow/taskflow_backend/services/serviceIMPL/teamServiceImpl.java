package com.taskFlow.taskflow_backend.services.serviceIMPL;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.taskFlow.taskflow_backend.dto.teamDTO;
import com.taskFlow.taskflow_backend.dto.taskDTO;
import com.taskFlow.taskflow_backend.model.Entity.Team;
import com.taskFlow.taskflow_backend.model.Entity.User;
import com.taskFlow.taskflow_backend.model.Entity.TeamMembers;
import com.taskFlow.taskflow_backend.model.enums.TeamRole;
import com.taskFlow.taskflow_backend.model.enums.UserRole;
import com.taskFlow.taskflow_backend.respository.teamMembersRepository;
import com.taskFlow.taskflow_backend.respository.teamRepository;
import com.taskFlow.taskflow_backend.respository.userRepositoty;
import com.taskFlow.taskflow_backend.services.teamService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class teamServiceImpl implements teamService {

    private final teamRepository teamRepository;
    private final teamMembersRepository teamMembersRepository;
    private final userRepositoty userRepository;

    // ===============================
    // CREATE TEAM
    // ===============================
    @Override
    public teamDTO.TeamResponseDTO createTeam(
            teamDTO.CreateTeamRequestDTO request,
            UUID creatorUserId) {

        User creator = userRepository.findById(creatorUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Only ADMIN / MANAGER can create team
        if (creator.getUserRole() == UserRole.MEMBER) {
            throw new RuntimeException("Not authorized to create team");
        }

        Team team = new Team();
        team.setTeamName(request.getTeamName());
        team.setCreatedBy(creator);

        Team savedTeam = teamRepository.save(team);

        // Add creator as TEAM LEAD
        TeamMembers teamMember = new TeamMembers();
        teamMember.setTeam(savedTeam);
        teamMember.setUser(creator);
        teamMember.setRoleInTeam(TeamRole.LEAD);

        teamMembersRepository.save(teamMember);

        return mapToTeamResponse(savedTeam);
    }

    // ===============================
    // GET ALL TEAMS
    // ===============================
    @Override
    public List<teamDTO.TeamSummaryDTO> getAllTeams() {

        return teamRepository.findAll()
                .stream()
                .map(team -> {
                    teamDTO.TeamSummaryDTO dto = new teamDTO.TeamSummaryDTO();
                    dto.setTeamId(team.getTeamId());
                    dto.setTeamName(team.getTeamName());
                    dto.setTotalTasks(team.getTasks().size());
                    return dto;
                })
                .toList();
    }

    // ===============================
    // GET TEAM BY ID
    // ===============================
    @Override
    public teamDTO.TeamResponseDTO getTeamById(UUID teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        return mapToTeamResponse(team);
    }

    // ===============================
    // ADD USER TO TEAM (RBAC)
    // ===============================
    @Override
    public teamDTO.TeamResponseDTO addUserToTeam(
            UUID teamId,
            UUID adminUserId,
            teamDTO.AddUserToTeamRequestDTO request) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        TeamMembers adminMembership = teamRepository.findByTeam_TeamIdAndUser_UserId(teamId, adminUserId)
                .orElseThrow(() -> new RuntimeException("Not a team member"));

        if (adminMembership.getRoleInTeam() != TeamRole.LEAD) {
            throw new RuntimeException("Only TEAM LEAD can add users");
        }

        User userToAdd = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (teamRepository.existsByTeam_TeamIdAndUser_UserId(teamId, userToAdd.getUserId())) {
            throw new RuntimeException("User already in team");
        }

        TeamMembers member = new TeamMembers();
        member.setTeam(team);
        member.setUser(userToAdd);
        member.setRoleInTeam(TeamRole.MEMBER);

        teamMembersRepository.save(member);

        return mapToTeamResponse(team);
    }

    // ===============================
    // MAPPER
    // ===============================
    private teamDTO.TeamResponseDTO mapToTeamResponse(Team team) {

        teamDTO.TeamResponseDTO dto = new teamDTO.TeamResponseDTO();
        dto.setTeamId(team.getTeamId());
        dto.setTeamName(team.getTeamName());
        dto.setCreatedByUserId(team.getCreatedBy().getUserId());
        dto.setCreatedByUserName(team.getCreatedBy().getName());

        dto.setTasks(
                team.getTasks()
                        .stream()
                        .map(task -> {
                            taskDTO.TaskSummaryDTO t = new taskDTO.TaskSummaryDTO();
                            t.setTaskId(task.getTaskId());
                            t.setTaskTitle(task.getTaskTitle());
                            t.setStatus(task.getStatus());
                            return t;
                        })
                        .toList());

        return dto;
    }
}
