package com.project.fitness.repository;

import com.project.fitness.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, UUID> {

    List<Recommendation> findByUserId(UUID userId);

    List<Recommendation> findByActivityId(UUID activityId);
}
