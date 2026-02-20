package com.project.fitness.dto;

import lombok.*;
import java.util.UUID;
import com.project.fitness.model.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private UUID id;
    private String email;
    private UserRole role;
}
