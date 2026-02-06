package com.phellipe.workoutplanner.backend.controller;

import com.phellipe.workoutplanner.backend.dto.workout.CreateWorkoutRequest;
import com.phellipe.workoutplanner.backend.dto.workout.ReorderWorkoutsRequest;
import com.phellipe.workoutplanner.backend.dto.workout.UpdateWorkoutRequest;
import com.phellipe.workoutplanner.backend.dto.workout.WorkoutResponse;
import com.phellipe.workoutplanner.backend.service.WorkoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping
    public ResponseEntity<WorkoutResponse> createWorkout(@Valid @RequestBody CreateWorkoutRequest request) {
        WorkoutResponse response = workoutService.createWorkout(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutResponse> updateWorkoutName(@PathVariable Long id, @Valid @RequestBody UpdateWorkoutRequest request) {
        WorkoutResponse response = workoutService.updateWorkoutName(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/workout-plan/{workoutPlanId}/reorder")
    public ResponseEntity<List<WorkoutResponse>> reorderWorkouts(@PathVariable Long workoutPlanId, @Valid @RequestBody ReorderWorkoutsRequest request) {
        List<WorkoutResponse> response = workoutService.reorderWorkouts(workoutPlanId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        workoutService.deleteWorkout(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutResponse> getWorkoutById(@PathVariable Long id) {
        WorkoutResponse response = workoutService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/workout-plan/{workoutPlanId}")
    public ResponseEntity<List<WorkoutResponse>> getWorkoutsByPlan(@PathVariable Long workoutPlanId) {
        List<WorkoutResponse> response = workoutService.findAllByWorkoutPlanId(workoutPlanId);
        return ResponseEntity.ok(response);
    }

}
