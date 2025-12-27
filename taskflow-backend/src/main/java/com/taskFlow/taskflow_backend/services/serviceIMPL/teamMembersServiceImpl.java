package com.taskFlow.taskflow_backend.services.serviceIMPL;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.taskFlow.taskflow_backend.dto.teamMembersDTO;
import com.taskFlow.taskflow_backend.model.Entity.Team;
import com.taskFlow.taskflow_backend.model.Entity.TeamMembers;
import com.taskFlow.taskflow_backend.model.Entity.User;
import com.taskFlow.taskflow_backend.model.enums.TeamRole;
import com.taskFlow.taskflow_backend.respository.teamMembersRepository;
import com.taskFlow.taskflow_backend.respository.teamRepository;
import com.taskFlow.taskflow_backend.respository.userRepositoty;
import com.taskFlow.taskflow_backend.services.teamMembersService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class teamMembersServiceImpl implements teamMembersService {

    private final teamMembersRepository teamMembersRepository;
    private final teamRepository teamRepository;
    private final userRepositoty userRepository;

    // ===============================
    // ADD MEMBER (ONLY LEAD)
    // ===============================
    @Override
    public teamMembersDTO.TeamMemberResponseDTO addMember(
            UUID teamId,
            UUID adminUserId,
            teamMembersDTO.AddTeamMemberRequestDTO request) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        TeamMembers admin = teamMembersRepository
                .findByTeam_TeamIdAndUser_UserId(teamId, adminUserId)
                .orElseThrow(() -> new RuntimeException("Not a team member"));

        if (admin.getRoleInTeam() != TeamRole.LEAD) {
            throw new RuntimeException("Only TEAM LEAD can add members");
        }

        if (teamMembersRepository.existsByTeam_TeamIdAndUser_UserId(teamId, request.getUserId())) {
            throw new RuntimeException("User already in team");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        TeamMembers member = new TeamMembers();
        member.setTeam(team);
        member.setUser(user);
        member.setRoleInTeam(request.getRoleInTeam());

        teamMembersRepository.save(member);

        return mapToResponse(member);
    }

    // ===============================
    // GET ALL MEMBERS
    // ===============================
    @Override
    public List<teamMembersDTO.TeamMemberResponseDTO> getAllMembers(UUID teamId) {

        return teamMembersRepository.findByTeam_TeamId(teamId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ===============================
    // REMOVE MEMBER (ONLY LEAD)
    // ===============================
    @Override
    public void removeMember(UUID teamId, UUID adminUserId, UUID userId) {

        TeamMembers admin = teamMembersRepository
                .findByTeam_TeamIdAndUser_UserId(teamId, adminUserId)
                .orElseThrow(() -> new RuntimeException("Not a team member"));

        if (admin.getRoleInTeam() != TeamRole.LEAD) {
            throw new RuntimeException("Only TEAM LEAD can remove members");
        }

        TeamMembers member = teamMembersRepository
                .findByTeam_TeamIdAndUser_UserId(teamId, userId)
                .orElseThrow(() -> new RuntimeException("User not in team"));

        teamMembersRepository.delete(member);
    }

    // ===============================
    // MAPPER
    // ===============================
    private teamMembersDTO.TeamMemberResponseDTO mapToResponse(TeamMembers member) {

        teamMembersDTO.TeamMemberResponseDTO dto = new teamMembersDTO.TeamMemberResponseDTO();

        dto.setTeamId(member.getTeam().getTeamId());
        dto.setUserId(member.getUser().getUserId());
        dto.setUserName(member.getUser().getName());
        dto.setUserEmail(member.getUser().getEmail());
        dto.setRoleInTeam(member.getRoleInTeam());

        return dto;
    }
}
