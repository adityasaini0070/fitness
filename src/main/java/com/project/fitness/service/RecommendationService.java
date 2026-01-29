package com.project.fitness.service;

import com.project.fitness.dto.RecommendationRequest;
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

        private final UserRepository userRepository;
        private final ActivityRepository activityRepository;
        private final RecommendationRepository recommendationRepository;

        public Recommendation generateRecommendation(RecommendationRequest request) {
                User user = userRepository.findById(UUID.fromString(request.getUserId()))
                                .orElseThrow(() -> new RuntimeException("user not found " + request.getUserId()));

                Activity activity = activityRepository.findById(UUID.fromString(request.getActivityId()))
                                .orElseThrow(() -> new RuntimeException(
                                                "activity not found " + request.getActivityId()));

                Recommendation recommendation = Recommendation.builder()
                                .user(user)
                                .activity(activity)
                                .type(request.getType() != null ? request.getType() : "GENERAL")
                                .recommendation(request.getRecommendation())
                                .improvements(request.getImprovements())
                                .suggestions(request.getSuggestions())
                                .safety(request.getSafety())
                                .build();

                return recommendationRepository.save(recommendation);
        }

        public List<Recommendation> getUserRecommendation(String userId) {
                try {
                        return recommendationRepository.findByUserId(UUID.fromString(userId));
                } catch (IllegalArgumentException e) {
                        return List.of();
                }
        }

        public List<Recommendation> getActivityRecommendation(String activityId) {
                try {
                        return recommendationRepository.findByActivityId(UUID.fromString(activityId));
                } catch (IllegalArgumentException e) {
                        return List.of();
                }
        }
}
