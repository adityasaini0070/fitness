package com.project.fitness.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;
import com.project.fitness.model.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
