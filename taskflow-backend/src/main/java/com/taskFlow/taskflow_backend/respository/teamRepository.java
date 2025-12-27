package com.taskFlow.taskflow_backend.respository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskFlow.taskflow_backend.model.Entity.Team;

public interface teamRepository extends JpaRepository<Team, UUID> {

}
