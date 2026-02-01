package com.phellipe.workoutplanner.backend.domain.repository;

import com.phellipe.workoutplanner.backend.domain.entity.WorkoutBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutBlockRepository extends JpaRepository<WorkoutBlock, Long> {

    List<WorkoutBlock> findByWorkoutIdOrderByPositionAsc(Long workoutId);

    @Query("SELECT COALESCE(MAX(b.position), 0) FROM WorkoutBlock b WHERE b.workout.id = :workoutId")
    Integer findMaxPositionByWorkoutId(Long workoutId);


}
