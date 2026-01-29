package com.phellipe.workoutplanner.backend.domain.repository;

import com.phellipe.workoutplanner.backend.domain.entity.WorkoutBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutBlockRepository extends JpaRepository<WorkoutBlock, Long> {
}
