package com.phellipe.workoutplanner.backend.dto.workout;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateWorkoutRequest {

    @NotBlank(message = "Workout name is required")
    private String name;
}
