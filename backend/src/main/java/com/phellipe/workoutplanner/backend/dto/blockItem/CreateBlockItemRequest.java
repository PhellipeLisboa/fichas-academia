package com.phellipe.workoutplanner.backend.dto.blockItem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateBlockItemRequest {

    @NotNull(message = "Workout block ID is required")
    private Long workoutBlockId;

    @NotNull(message = "Exercise ID is required")
    private Long exerciseId;

    @NotNull(message = "Machine ID is required")
    private Long machineId;

    @NotNull(message = "Sets is required")
    @Positive(message = "Sets must be positive")
    private Integer sets;

    @NotNull(message = "Reps is required")
    @Positive(message = "Reps must be positive")
    private Integer reps;

}
