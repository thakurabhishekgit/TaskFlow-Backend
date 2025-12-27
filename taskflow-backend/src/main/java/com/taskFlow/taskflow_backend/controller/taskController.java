package com.taskFlow.taskflow_backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.taskFlow.taskflow_backend.dto.taskDTO;
import com.taskFlow.taskflow_backend.services.taskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
public class taskController {

    private final taskService taskService;

    // CREATE TASK
    @PostMapping("/create/{creatorUserId}")
    public ResponseEntity<taskDTO.TaskResponseDTO> createTask(
            @PathVariable UUID creatorUserId,
            @Valid @RequestBody taskDTO.CreateTaskRequestDTO request) {

        return ResponseEntity.ok(
                taskService.createTask(creatorUserId, request));
    }

    // UPDATE TASK STATUS
    @PatchMapping("/{taskId}/status/{userId}")
    public ResponseEntity<taskDTO.TaskResponseDTO> updateTaskStatus(
            @PathVariable UUID taskId,
            @PathVariable UUID userId,
            @Valid @RequestBody taskDTO.UpdateTaskStatusRequestDTO request) {

        return ResponseEntity.ok(
                taskService.updateTaskStatus(taskId, userId, request));
    }

    // GET TASKS BY TEAM
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<taskDTO.TaskResponseDTO>> getTasksByTeam(
            @PathVariable UUID teamId) {

        return ResponseEntity.ok(
                taskService.getTasksByTeam(teamId));
    }

    // GET TASKS ASSIGNED TO USER
    @GetMapping("/assigned/{userId}")
    public ResponseEntity<List<taskDTO.TaskResponseDTO>> getTasksAssignedToUser(
            @PathVariable UUID userId) {

        return ResponseEntity.ok(
                taskService.getTasksAssignedToUser(userId));
    }
}
