package com.phellipe.workoutplanner.backend.service;

import com.phellipe.workoutplanner.backend.domain.entity.Workout;
import com.phellipe.workoutplanner.backend.domain.entity.WorkoutPlan;
import com.phellipe.workoutplanner.backend.domain.enumtype.WorkoutPlanStatus;
import com.phellipe.workoutplanner.backend.domain.repository.WorkoutRepository;
import com.phellipe.workoutplanner.backend.dto.workout.CreateWorkoutRequest;
import com.phellipe.workoutplanner.backend.dto.workout.ReorderWorkoutsRequest;
import com.phellipe.workoutplanner.backend.dto.workout.UpdateWorkoutRequest;
import com.phellipe.workoutplanner.backend.dto.workout.WorkoutResponse;
import com.phellipe.workoutplanner.backend.exception.BusinessException;
import com.phellipe.workoutplanner.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final WorkoutPlanService workoutPlanService;

    @Transactional
    public WorkoutResponse createWorkout(CreateWorkoutRequest request) {
        WorkoutPlan workoutPlan = workoutPlanService.getWorkoutPlanEntity(request.getWorkoutPlanId());

        if (workoutPlan.getStatus() != WorkoutPlanStatus.DRAFT) {
            throw new BusinessException("Cannot add workout to finalized plan");
        }

        Integer maxPosition = workoutRepository.findMaxPositionByWorkoutPlanId(workoutPlan.getId());
        Integer nextPosition = maxPosition + 1;

        Workout workout = new Workout();
        workout.setWorkoutPlan(workoutPlan);
        workout.setName(request.getName().trim().toUpperCase());
        workout.setPosition(nextPosition);

        Workout savedWorkout = workoutRepository.save(workout);
        return WorkoutResponse.from(savedWorkout);
    }

    @Transactional
    public WorkoutResponse updateWorkoutName(Long workoutId, UpdateWorkoutRequest request) {
        Workout workout = getWorkoutEntity(workoutId);

        if (workout.getWorkoutPlan().getStatus() == WorkoutPlanStatus.INACTIVE) {
            throw new BusinessException("Cannot update workout in inactive plan");
        }

        workout.setName(request.getName().trim().toUpperCase());

        Workout updatedWorkout = workoutRepository.save(workout);
        return WorkoutResponse.from(updatedWorkout);

    }

    @Transactional
    public List<WorkoutResponse> reorderWorkouts(Long workoutPlanId, ReorderWorkoutsRequest request) {
        WorkoutPlan workoutPlan = workoutPlanService.getWorkoutPlanEntity(workoutPlanId);

        if (workoutPlan.getStatus() == WorkoutPlanStatus.INACTIVE) {
            throw new BusinessException("Cannot reorder workout in inactive plan");
        }

        List<Workout> workouts = workoutRepository.findByWorkoutPlanIdOrderByPositionAsc(workoutPlanId);

        if (workouts.size() != request.getWorkoutIds().size()) {
            throw new BusinessException("Workout IDs count mismatch");
        }

        for (Long workoutId : request.getWorkoutIds()) {
            boolean found = workouts
                    .stream()
                    .anyMatch(w -> w.getId().equals(workoutId));
            if (!found) {
                throw new BusinessException("Workout ID " + workoutId + " does not belong to this plan");
            }
        }

        for (int i = 0; i < request.getWorkoutIds().size(); i++) {
            Long workoutId = request.getWorkoutIds().get(i);
            Workout workout = workouts.stream()
                    .filter(w -> w.getId().equals(workoutId))
                    .findFirst()
                    .orElseThrow();

            workout.setPosition(i + 1);
        }

        List<Workout> reorderWorkouts = workoutRepository.saveAll(workouts);

        return reorderWorkouts
                .stream()
                .map(WorkoutResponse::from)
                .toList();
    }

    @Transactional
    public void deleteWorkout(Long workoutId) {
        Workout workout = getWorkoutEntity(workoutId);

        if (workout.getWorkoutPlan().getStatus() != WorkoutPlanStatus.DRAFT) {
            throw new BusinessException("Cannot delete workout from finalized plan");
        }

        workoutRepository.delete(workout);

        List<Workout> remainingWorkouts = workoutRepository.findByWorkoutPlanIdOrderByPositionAsc(workout.getWorkoutPlan().getId());

        for (int i = 0; i < remainingWorkouts.size(); i++) {
            remainingWorkouts.get(i).setPosition(i + 1);
        }

        workoutRepository.saveAll(remainingWorkouts);

    }

    public List<WorkoutResponse> findAllByWorkoutPlanId(Long workoutPlanId) {
        return workoutRepository.findByWorkoutPlanIdOrderByPositionAsc(workoutPlanId)
                .stream()
                .map(WorkoutResponse::from)
                .toList();
    }

    public WorkoutResponse findById(Long workoutId) {
        Workout workout = getWorkoutEntity(workoutId);
        return WorkoutResponse.from(workout);
    }

    public Workout getWorkoutEntity(Long id) {
        return workoutRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Workout not found with id: " + id)
        );
    }

}
