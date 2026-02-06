package com.project.fitness.dto;

import com.project.fitness.model.ActivityType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityResponse {
    private UUID id;
    private ActivityType type;
    private Map<String, Object> additionalMetrics;
    private Integer duration;
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private UUID userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
