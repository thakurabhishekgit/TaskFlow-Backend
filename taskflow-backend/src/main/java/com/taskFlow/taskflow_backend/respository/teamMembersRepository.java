package com.taskFlow.taskflow_backend.respository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskFlow.taskflow_backend.model.Entity.TeamMembers;

public interface teamMembersRepository extends JpaRepository<TeamMembers, UUID> {

}
