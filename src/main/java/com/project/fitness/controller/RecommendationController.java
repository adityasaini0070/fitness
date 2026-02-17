package com.project.fitness.controller;

import com.project.fitness.dto.RecommendationResponse;
import com.project.fitness.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping
    public ResponseEntity<RecommendationResponse> getRecommendations(
            Authentication authentication) {

        UUID userId = UUID.fromString(authentication.getPrincipal().toString());

        return ResponseEntity.ok(
                recommendationService.generateRecommendations(userId));
    }
}
