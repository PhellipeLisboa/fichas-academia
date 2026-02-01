package com.phellipe.workoutplanner.backend.domain.repository;

import com.phellipe.workoutplanner.backend.domain.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    List<Workout> findByWorkoutPlanIdOrderByPositionAsc(Long workoutPlanId);

    @Query("SELECT COALESCE(MAX(w.position), 0) FROM Workout w WHERE w.workoutPlan.id = :workoutPlanId")
    Integer findMaxPositionByWorkoutPlanId(Long workoutPlanId);

}
