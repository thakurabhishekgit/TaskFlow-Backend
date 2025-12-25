package com.taskFlow.taskflow_backend.payload;

import com.taskFlow.taskflow_backend.dto.userDTO;

import lombok.Data;

@Data
public class TokenWithUserRequest {
    private String token;
    private userDTO user;

    public TokenWithUserRequest(String token, userDTO user) {
        this.token = token;
        this.user = user;
    }

}
