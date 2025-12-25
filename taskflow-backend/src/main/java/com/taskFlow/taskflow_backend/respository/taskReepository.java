package com.taskFlow.taskflow_backend.respository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskFlow.taskflow_backend.model.Entity.Task;

public interface taskReepository extends JpaRepository<Task, UUID> {

}
