package com.project.fitness.service;

import com.project.fitness.dto.RecommendationResponse;
import com.project.fitness.model.Activity;
import com.project.fitness.model.Recommendation;
import com.project.fitness.model.User;
import com.project.fitness.repository.ActivityRepository;
import com.project.fitness.repository.RecommendationRepository;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecommendationService {

        private final ActivityRepository activityRepository;
        private final RecommendationRepository recommendationRepository;
        private final UserRepository userRepository;

        public RecommendationResponse generateRecommendations(UUID userId) {

                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                List<Activity> activities = activityRepository.findByUserId(userId);

                if (activities.isEmpty()) {
                        return new RecommendationResponse(
                                        "Start Your Fitness Journey 🚀",
                                        "You haven’t logged any activities yet. Begin with light cardio.",
                                        0,
                                        "Beginner");
                }

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

                List<String> improvements;
                List<String> suggestions;
                List<String> safety;

                if (totalCalories >= 2500) {
                        title = "High Performance Mode 🔥";
                        message = "You are maintaining strong activity levels.";
                        level = "Advanced";

                        improvements = List.of("Maintain recovery routine", "Track protein intake");
                        suggestions = List.of("Include mobility sessions", "Try progressive overload");
                        safety = List.of("Avoid overtraining", "Ensure proper hydration");
                }

                else if (cardioCount > strengthCount) {
                        title = "Balance Your Routine 💪";
                        message = "Add more resistance training to your routine.";
                        level = "Intermediate";

                        improvements = List.of("Increase strength frequency", "Monitor muscle fatigue");
                        suggestions = List.of("Add compound lifts", "Track weekly strength progress");
                        safety = List.of("Warm up properly", "Use correct lifting form");
                }

                else if (totalDuration < 90) {
                        title = "Increase Weekly Activity ⏱";
                        message = "Try increasing your workout duration gradually.";
                        level = "Beginner";

                        improvements = List.of("Increase weekly minutes", "Set short-term goals");
                        suggestions = List.of("Add 10-minute sessions", "Track consistency");
                        safety = List.of("Avoid sudden volume spikes", "Stretch after workouts");
                }

                else {
                        title = "Steady Progress 📈";
                        message = "You are building consistency.";
                        level = "Growing";

                        improvements = List.of("Increase intensity gradually");
                        suggestions = List.of("Track performance metrics");
                        safety = List.of("Maintain proper rest cycles");
                }

                /* ========= Use Latest Activity For Relation ========= */

                Activity latestActivity = activities.get(activities.size() - 1);

                Recommendation recommendation = Recommendation.builder()
                                .type(level)
                                .recommendation(message)
                                .improvements(improvements)
                                .suggestions(suggestions)
                                .safety(safety)
                                .user(user)
                                .activity(latestActivity)
                                .build();

                recommendationRepository.save(recommendation);

                return new RecommendationResponse(
                                title,
                                message,
                                totalCalories,
                                level);
        }
}