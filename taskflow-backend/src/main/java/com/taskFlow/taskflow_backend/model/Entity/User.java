package com.taskFlow.taskflow_backend.model.Entity;

import com.taskFlow.taskflow_backend.model.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID userId;

    @NotBlank(message = "Username cannot be blank")
    @NotNull
    @Size(min = 3, max = 15)
    private String name;

    @Email
    @NotNull
    @NotBlank(message = "Email cannot be null")
    @Column(unique = true)
    private String email;

    @NotNull
    @NotBlank(message = "password cannot be blank")
    @Size(min = 4, max = 15)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "assignedTo")
    private List<Task> assignedTasks = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
