package com.taskFlow.taskflow_backend.services;

import java.util.UUID;

import com.taskFlow.taskflow_backend.dto.userDTO;

public interface userService {

    userDTO registerUser(userDTO userDTO);

    userDTO getUserById(UUID userId);

    userDTO updateUser(UUID userId, userDTO userDTO);

    userDTO loginUser(String email, String password);

}
