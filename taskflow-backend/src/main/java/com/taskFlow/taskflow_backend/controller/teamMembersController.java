package com.taskFlow.taskflow_backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.taskFlow.taskflow_backend.dto.teamMembersDTO;
import com.taskFlow.taskflow_backend.services.teamMembersService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/teams/{teamId}/members")
public class teamMembersController {

    private final teamMembersService teamMembersService;

    // ADD MEMBER
    @PostMapping("/admin/{adminUserId}")
    public ResponseEntity<teamMembersDTO.TeamMemberResponseDTO> addMember(
            @PathVariable UUID teamId,
            @PathVariable UUID adminUserId,
            @Valid @RequestBody teamMembersDTO.AddTeamMemberRequestDTO request) {

        return ResponseEntity.ok(
                teamMembersService.addMember(teamId, adminUserId, request));
    }

    // GET MEMBERS
    @GetMapping
    public ResponseEntity<List<teamMembersDTO.TeamMemberResponseDTO>> getMembers(
            @PathVariable UUID teamId) {

        return ResponseEntity.ok(
                teamMembersService.getAllMembers(teamId));
    }

    // REMOVE MEMBER
    @DeleteMapping("/{userId}/admin/{adminUserId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable UUID teamId,
            @PathVariable UUID adminUserId,
            @PathVariable UUID userId) {

        teamMembersService.removeMember(teamId, adminUserId, userId);
        return ResponseEntity.noContent().build();
    }
}
