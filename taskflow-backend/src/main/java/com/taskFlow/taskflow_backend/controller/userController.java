package com.taskFlow.taskflow_backend.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskFlow.taskflow_backend.config.JWTconfig.JWTconfig;
import com.taskFlow.taskflow_backend.dto.userDTO;
import com.taskFlow.taskflow_backend.dto.userDTO.UserLoginRequestDTO;
import com.taskFlow.taskflow_backend.payload.TokenWithUserRequest;
import com.taskFlow.taskflow_backend.services.userService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class userController {

    @Autowired
    private final userService userService;

    @PostMapping("/register")
    public ResponseEntity<TokenWithUserRequest> registerUser(@Valid @RequestBody userDTO userDTO) {
        userDTO registeredUser = userService.registerUser(userDTO);
        String token = JWTconfig.generateToken(registeredUser.getEmail());
        TokenWithUserRequest response = new TokenWithUserRequest(token, registeredUser);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenWithUserRequest> loginUser(@Valid @RequestBody UserLoginRequestDTO loginRequest) {
        userDTO loggedInUser = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        if (loggedInUser == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        String token = JWTconfig.generateToken(loggedInUser.getEmail());
        TokenWithUserRequest tokenWithUserRequest = new TokenWithUserRequest(token, loggedInUser);
        return ResponseEntity.ok(tokenWithUserRequest);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<userDTO> getUserById(@PathVariable UUID userId) {
        userDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<userDTO> updateUser(@Valid @PathVariable UUID userId, @RequestBody userDTO userDTO) {
        userDTO updatedUser = userService.updateUser(userId, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}