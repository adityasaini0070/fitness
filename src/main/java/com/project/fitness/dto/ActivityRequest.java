package com.project.fitness.dto;

import com.project.fitness.model.ActivityType;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRequest {
    private UUID userId;
    private ActivityType type;
    private Map<String, Object> additionalMetrics;
    private Integer duration;
    private Integer caloriesBurned;
    @JsonFormat(pattern = "yyyy-MM-dd['T'][ ]HH:mm:ss[.SSSSSS]")
    private LocalDateTime startTime;
}
