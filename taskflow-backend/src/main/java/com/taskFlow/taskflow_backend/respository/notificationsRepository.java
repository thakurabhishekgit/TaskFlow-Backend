package com.taskFlow.taskflow_backend.respository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskFlow.taskflow_backend.model.Entity.Notification;

public interface notificationsRepository extends JpaRepository<Notification, UUID> {

}
