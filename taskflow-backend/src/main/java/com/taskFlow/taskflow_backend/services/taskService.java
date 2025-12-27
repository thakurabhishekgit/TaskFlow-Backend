package com.taskFlow.taskflow_backend.services;

import java.util.List;
import java.util.UUID;

import com.taskFlow.taskflow_backend.dto.taskDTO;

public interface taskService {

    taskDTO.TaskResponseDTO createTask(
            UUID creatorUserId,
            taskDTO.CreateTaskRequestDTO request);

    taskDTO.TaskResponseDTO updateTaskStatus(
            UUID taskId,
            UUID userId,
            taskDTO.UpdateTaskStatusRequestDTO request);

    List<taskDTO.TaskResponseDTO> getTasksByTeam(UUID teamId);

    List<taskDTO.TaskResponseDTO> getTasksAssignedToUser(UUID userId);
}
