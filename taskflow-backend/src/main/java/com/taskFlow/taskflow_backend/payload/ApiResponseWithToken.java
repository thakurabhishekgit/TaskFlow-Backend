package com.taskFlow.taskflow_backend.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponseWithToken<T> {
    private String token;
    private T data;
}
