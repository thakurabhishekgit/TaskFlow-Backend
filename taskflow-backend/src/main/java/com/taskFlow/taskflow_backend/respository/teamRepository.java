package com.taskFlow.taskflow_backend.respository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskFlow.taskflow_backend.model.Entity.Team;
import com.taskFlow.taskflow_backend.model.Entity.TeamMembers;

public interface teamRepository extends JpaRepository<Team, UUID> {
    boolean existsByTeam_TeamIdAndUser_UserId(UUID teamId, UUID userId);

    Optional<TeamMembers> findByTeam_TeamIdAndUser_UserId(UUID teamId, UUID userId);

    List<TeamMembers> findByTeam_TeamId(UUID teamId);

}
