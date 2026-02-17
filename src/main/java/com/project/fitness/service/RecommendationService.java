package com.project.fitness.service;

import com.project.fitness.dto.RecommendationResponse;
import com.project.fitness.model.Activity;
import com.project.fitness.repository.ActivityRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecommendationService {

        private final ActivityRepository activityRepository;

        public RecommendationResponse generateRecommendations(UUID userId) {

                List<Activity> activities = activityRepository.findByUserId(userId);

                if (activities.isEmpty()) {
                        return new RecommendationResponse(
                                        "Start Your Fitness Journey 🚀",
                                        "You haven’t logged any activities yet. Begin with 20 minutes of walking or light cardio to build momentum.",
                                        0,
                                        "Beginner");
                }

                /* ================= CALCULATIONS ================= */

                int totalCalories = activities.stream()
                                .mapToInt(a -> a.getCaloriesBurned() != null ? a.getCaloriesBurned() : 0)
                                .sum();

                int totalDuration = activities.stream()
                                .mapToInt(a -> a.getDuration() != null ? a.getDuration() : 0)
                                .sum();

                long cardioCount = activities.stream()
                                .filter(a -> a.getType().name().contains("RUN") ||
                                                a.getType().name().contains("WALK") ||
                                                a.getType().name().contains("CYCLE") ||
                                                a.getType().name().contains("SWIM"))
                                .count();

                long strengthCount = activities.stream()
                                .filter(a -> a.getType().name().contains("WEIGHT"))
                                .count();

                String title;
                String message;
                String level;

                if (totalCalories >= 2500) {
                        title = "High Performance Mode 🔥";
                        message = "You are maintaining a strong activity level. Ensure proper recovery, hydration, and protein intake.";
                        level = "Advanced";
                }

                else if (cardioCount > strengthCount) {
                        title = "Balance Your Routine 💪";
                        message = "You are doing more cardio than strength training. Consider adding weight sessions for muscle balance.";
                        level = "Intermediate";
                }

                else if (totalDuration < 90) {
                        title = "Increase Weekly Activity ⏱";
                        message = "Your total workout duration is on the lower side. Try adding short 15-minute sessions.";
                        level = "Beginner";
                }

                else {
                        title = "Steady Progress 📈";
                        message = "You are building consistency. Keep increasing intensity gradually to avoid plateaus.";
                        level = "Growing";
                }

                return new RecommendationResponse(
                                title,
                                message,
                                totalCalories,
                                level);
        }
}
