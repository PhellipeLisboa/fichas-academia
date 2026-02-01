package com.phellipe.workoutplanner.backend.dto.blockItem;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MoveBlockItemRequest {

    @NotNull(message = "Target block ID is required")
    private Long targetBlockId;
}
