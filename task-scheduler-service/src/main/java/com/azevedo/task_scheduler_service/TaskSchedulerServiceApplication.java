package com.azevedo.task_scheduler_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TaskSchedulerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskSchedulerServiceApplication.class, args);
	}

}
