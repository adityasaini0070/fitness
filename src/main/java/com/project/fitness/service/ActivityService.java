package com.project.fitness.service;

import com.project.fitness.dto.ActivityRequest;
import com.project.fitness.dto.ActivityResponse;
import com.project.fitness.model.Activity;
import com.project.fitness.model.User;
import com.project.fitness.repository.ActivityRepository;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public ActivityResponse trackActivity(ActivityRequest request, UUID userId) {

        @SuppressWarnings("null")
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Invalid user: " + userId));

        Activity activity = Activity.builder()
                .user(user)
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();
        @SuppressWarnings("null")
        Activity savedActivity = activityRepository.save(activity);
        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity activity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUser().getId());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        return response;
    }

    public List<ActivityResponse> getUserActivities(UUID userId) {
        try {
            List<Activity> activityList = activityRepository.findByUserId(userId);
            return activityList.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }

    public void deleteActivity(UUID activityId, UUID userId) {

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        if (!activity.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        activityRepository.delete(activity);
    }

    public ActivityResponse updateActivity(
            UUID activityId,
            ActivityRequest request,
            UUID userId) {

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        if (!activity.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        activity.setType(request.getType());
        activity.setDuration(request.getDuration());
        activity.setCaloriesBurned(request.getCaloriesBurned());
        activity.setStartTime(request.getStartTime());
        activity.setAdditionalMetrics(request.getAdditionalMetrics());

        Activity updated = activityRepository.save(activity);

        return mapToResponse(updated);
    }

}
