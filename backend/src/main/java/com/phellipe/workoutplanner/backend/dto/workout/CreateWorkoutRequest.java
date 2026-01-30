package com.phellipe.workoutplanner.backend.dto.workout;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateWorkoutRequest {

    @NotNull(message = "Workout plan ID is required")
    private Long workoutPlanId;

    @NotBlank(message = "Workout name is required")
    private String name;
}
