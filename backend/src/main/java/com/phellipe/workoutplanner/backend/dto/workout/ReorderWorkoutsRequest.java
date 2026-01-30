package com.phellipe.workoutplanner.backend.dto.workout;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ReorderWorkoutsRequest {

    @NotEmpty(message = "Workout IDs list cannot be empty")
    private List<Long> workoutIds;
}
