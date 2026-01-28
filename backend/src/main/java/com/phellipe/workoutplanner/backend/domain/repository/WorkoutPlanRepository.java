package com.phellipe.workoutplanner.backend.domain.repository;

import com.phellipe.workoutplanner.backend.domain.entity.WorkoutPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {
}
