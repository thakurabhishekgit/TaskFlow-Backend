package com.taskFlow.taskflow_backend.model.Entity;

import com.taskFlow.taskflow_backend.model.enums.TaskPriority;
import com.taskFlow.taskflow_backend.model.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task")
public class Task {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    @GeneratedValue
    private UUID taskId;

    @NotBlank(message = "taskTitle cannot be blank")
    @NotNull
    @Size(min = 3, max = 15)
    private String taskTitle;

    @NotBlank(message = "taskDescription cannot be blank")
    @NotNull
    @Size(min = 3, max = 150)
    private String taskDescription;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
