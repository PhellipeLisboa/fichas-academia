package com.phellipe.workoutplanner.backend.dto.workoutBlock;

import com.phellipe.workoutplanner.backend.domain.enumtype.ExecutionType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateWorkoutBlockRequest {

    @NotNull(message = "Workout ID is required")
    private Long workoutId;

    @NotNull(message = "Execution type is required")
    private ExecutionType executionType;

}
