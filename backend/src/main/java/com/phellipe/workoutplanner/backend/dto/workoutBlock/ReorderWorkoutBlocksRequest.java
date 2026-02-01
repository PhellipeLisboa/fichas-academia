package com.phellipe.workoutplanner.backend.dto.workoutBlock;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ReorderWorkoutBlocksRequest {

    @NotEmpty(message = "Block IDs list cannot be empty")
    private List<Long> blockIds;
}
