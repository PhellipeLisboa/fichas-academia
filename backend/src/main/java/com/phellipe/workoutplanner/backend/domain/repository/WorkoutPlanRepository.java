package com.phellipe.workoutplanner.backend.domain.repository;

import com.phellipe.workoutplanner.backend.domain.entity.WorkoutPlan;
import com.phellipe.workoutplanner.backend.domain.enumtype.WorkoutPlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {

    List<WorkoutPlan> findByMemberId(Long memberId);
    List<WorkoutPlan> findByProfessionalId(Long professionalId);
    Optional<WorkoutPlan> findByMemberIdAndStatus(Long memberId, WorkoutPlanStatus status);
    List<WorkoutPlan> findAllByMemberIdAndStatus(Long memberId, WorkoutPlanStatus status);
    Optional<WorkoutPlan> findByMemberPublicCodeAndStatus(String publicCode, WorkoutPlanStatus status);


}
