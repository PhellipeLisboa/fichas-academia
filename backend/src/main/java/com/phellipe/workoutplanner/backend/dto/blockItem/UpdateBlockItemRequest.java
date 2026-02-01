package com.phellipe.workoutplanner.backend.dto.blockItem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateBlockItemRequest {

    private Long exerciseId;

    private Long machineId;

    @Positive(message = "Sets must be positive")
    private Integer sets;

    @Positive(message = "Reps must be positive")
    private Integer reps;

}
