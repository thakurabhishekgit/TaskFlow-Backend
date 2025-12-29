package com.taskFlow.taskflow_backend.respository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.taskFlow.taskflow_backend.model.Entity.User;

import io.lettuce.core.dynamic.annotation.Param;

public interface userRepositoty extends JpaRepository<User, UUID> {
    User findByEmail(String email);

    @Query("""
                SELECT u FROM User u
                LEFT JOIN FETCH u.assignedTasks
                WHERE u.userId = :userId
            """)
    Optional<User> findUserWithTasks(@Param("userId") UUID userId);
}
