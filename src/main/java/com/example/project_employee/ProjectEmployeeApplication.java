package com.example.project_employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ProjectEmployeeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectEmployeeApplication.class, args);
    }
}

