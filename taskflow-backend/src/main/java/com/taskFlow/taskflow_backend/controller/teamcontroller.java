package com.taskFlow.taskflow_backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskFlow.taskflow_backend.dto.teamDTO;
import com.taskFlow.taskflow_backend.services.teamService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/teams")
public class teamcontroller {

    @Autowired
    private teamService teamService;

    @PostMapping("/createTeam/{creatorUserId}")
    public ResponseEntity<teamDTO.TeamResponseDTO> createTeam(
            @Valid @RequestBody teamDTO.CreateTeamRequestDTO request, @PathVariable UUID creatorUserId) {
        teamDTO.TeamResponseDTO team = teamService.createTeam(request, creatorUserId); // null for creatorUserId
        return ResponseEntity.ok(team);

    }

    @GetMapping("/getAllTeams")
    public ResponseEntity<List<teamDTO.TeamSummaryDTO>> getAllTeams() {
        List<teamDTO.TeamSummaryDTO> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/getTeamById/{teamId}")
    public ResponseEntity<teamDTO.TeamResponseDTO> getTeamById(@PathVariable UUID teamId) {
        teamDTO.TeamResponseDTO team = teamService.getTeamById(teamId);
        return ResponseEntity.ok(team);
    }

    @PostMapping("/addUserToTeam/{teamId}")
    public ResponseEntity<teamDTO.TeamResponseDTO> addUserToTeam(
            @PathVariable UUID teamId,
            @Valid @RequestBody teamDTO.AddUserToTeamRequestDTO request) {
        teamDTO.TeamResponseDTO team = teamService.addUserToTeam(teamId, null, request); // null for adminUserId
        return ResponseEntity.ok(team);
    }

}
