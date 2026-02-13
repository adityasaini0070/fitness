package com.project.fitness.controller;

import com.project.fitness.dto.ActivityRequest;
import com.project.fitness.dto.ActivityResponse;
import com.project.fitness.service.ActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping
    public ResponseEntity<ActivityResponse> trackActivity(
            @RequestBody ActivityRequest request,
            Authentication authentication) {

        UUID userId = UUID.fromString(authentication.getPrincipal().toString());

        return ResponseEntity.ok(
                activityService.trackActivity(request, userId));
    }

    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getUserActivities(
            Authentication authentication) {

        UUID userId = UUID.fromString(authentication.getPrincipal().toString());

        return ResponseEntity.ok(
                activityService.getUserActivities(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(
            @PathVariable UUID id,
            Authentication authentication) {

        UUID userId = UUID.fromString(authentication.getName());

        activityService.deleteActivity(id, userId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityResponse> updateActivity(
            @PathVariable UUID id,
            @RequestBody ActivityRequest request,
            Authentication authentication) {

        UUID userId = UUID.fromString(authentication.getName());

        return ResponseEntity.ok(
                activityService.updateActivity(id, request, userId));
    }

}
