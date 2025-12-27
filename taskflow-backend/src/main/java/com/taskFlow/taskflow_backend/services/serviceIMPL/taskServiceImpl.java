package com.taskFlow.taskflow_backend.services.serviceIMPL;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.taskFlow.taskflow_backend.dto.taskDTO;
import com.taskFlow.taskflow_backend.model.Entity.Task;
import com.taskFlow.taskflow_backend.model.Entity.Team;
import com.taskFlow.taskflow_backend.model.Entity.TeamMembers;
import com.taskFlow.taskflow_backend.model.Entity.User;
import com.taskFlow.taskflow_backend.model.enums.TaskStatus;
import com.taskFlow.taskflow_backend.model.enums.TeamRole;
import com.taskFlow.taskflow_backend.respository.taskReepository;
import com.taskFlow.taskflow_backend.respository.teamMembersRepository;
import com.taskFlow.taskflow_backend.respository.teamRepository;
import com.taskFlow.taskflow_backend.respository.userRepositoty;
import com.taskFlow.taskflow_backend.services.taskService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class taskServiceImpl implements taskService {

        private final taskReepository taskRepository;
        private final teamRepository teamRepository;
        private final teamMembersRepository teamMembersRepository;
        private final userRepositoty userRepository;

        // ===============================
        // CREATE TASK (TEAM LEAD ONLY)
        // ===============================
        @Override
        public taskDTO.TaskResponseDTO createTask(
                        UUID creatorUserId,
                        taskDTO.CreateTaskRequestDTO request) {

                User creator = userRepository.findById(creatorUserId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                Team team = teamRepository.findById(request.getTeamId())
                                .orElseThrow(() -> new RuntimeException("Team not found"));

                TeamMembers creatorMembership = teamMembersRepository.findByTeam_TeamIdAndUser_UserId(
                                team.getTeamId(), creatorUserId)
                                .orElseThrow(() -> new RuntimeException("Not a team member"));

                if (creatorMembership.getRoleInTeam() != TeamRole.LEAD) {
                        throw new RuntimeException("Only TEAM LEAD can create tasks");
                }

                User assignedUser = userRepository.findById(request.getAssignedUserId())
                                .orElseThrow(() -> new RuntimeException("Assigned user not found"));

                if (!teamMembersRepository.existsByTeam_TeamIdAndUser_UserId(
                                team.getTeamId(), assignedUser.getUserId())) {
                        throw new RuntimeException("Assigned user is not part of the team");
                }

                Task task = new Task();
                task.setTaskTitle(request.getTaskTitle());
                task.setTaskDescription(request.getTaskDescription());
                task.setPriority(request.getPriority());
                task.setStatus(TaskStatus.TODO);
                task.setTeam(team);
                task.setAssignedTo(assignedUser);
                task.setCreatedBy(creator);

                LocalDateTime now = LocalDateTime.now();
                task.setCreatedAt(now);
                task.setUpdatedAt(now);

                Task savedTask = taskRepository.save(task);

                return mapToTaskResponse(savedTask);
        }

        // ===============================
        // UPDATE TASK STATUS (ASSIGNED USER)
        // ===============================
        @Override
        public taskDTO.TaskResponseDTO updateTaskStatus(
                        UUID taskId,
                        UUID userId,
                        taskDTO.UpdateTaskStatusRequestDTO request) {

                Task task = taskRepository.findById(taskId)
                                .orElseThrow(() -> new RuntimeException("Task not found"));

                if (!task.getAssignedTo().getUserId().equals(userId)) {
                        throw new RuntimeException("Only assigned user can update task status");
                }

                task.setStatus(request.getStatus());
                task.setUpdatedAt(LocalDateTime.now());

                return mapToTaskResponse(task);
        }

        // ===============================
        // GET TASKS BY TEAM
        // ===============================
        @Override
        public List<taskDTO.TaskResponseDTO> getTasksByTeam(UUID teamId) {

                return taskRepository.findAll()
                                .stream()
                                .filter(task -> task.getTeam().getTeamId().equals(teamId))
                                .map(this::mapToTaskResponse)
                                .toList();
        }

        // ===============================
        // GET TASKS ASSIGNED TO USER
        // ===============================
        @Override
        public List<taskDTO.TaskResponseDTO> getTasksAssignedToUser(UUID userId) {

                return taskRepository.findAll()
                                .stream()
                                .filter(task -> task.getAssignedTo() != null
                                                && task.getAssignedTo().getUserId().equals(userId))
                                .map(this::mapToTaskResponse)
                                .toList();
        }

        // ===============================
        // MAPPER
        // ===============================
        private taskDTO.TaskResponseDTO mapToTaskResponse(Task task) {

                taskDTO.TaskResponseDTO dto = new taskDTO.TaskResponseDTO();

                dto.setTaskId(task.getTaskId());
                dto.setTaskTitle(task.getTaskTitle());
                dto.setTaskDescription(task.getTaskDescription());
                dto.setStatus(task.getStatus());
                dto.setPriority(task.getPriority());

                dto.setAssignedUserId(task.getAssignedTo().getUserId());
                dto.setAssignedUserName(task.getAssignedTo().getName());

                dto.setTeamId(task.getTeam().getTeamId());
                dto.setTeamName(task.getTeam().getTeamName());

                dto.setCreatedByUserId(task.getCreatedBy().getUserId());
                dto.setCreatedByUserName(task.getCreatedBy().getName());

                dto.setCreatedAt(task.getCreatedAt());
                dto.setUpdatedAt(task.getUpdatedAt());

                return dto;
        }
}
