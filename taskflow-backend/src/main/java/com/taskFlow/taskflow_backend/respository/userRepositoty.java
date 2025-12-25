package com.taskFlow.taskflow_backend.respository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskFlow.taskflow_backend.model.Entity.User;

public interface userRepositoty extends JpaRepository<User, UUID> {
    User findByEmail(String email);

}
