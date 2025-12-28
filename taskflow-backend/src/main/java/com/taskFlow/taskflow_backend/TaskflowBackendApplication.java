package com.taskFlow.taskflow_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TaskflowBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskflowBackendApplication.class, args);
	}

}
