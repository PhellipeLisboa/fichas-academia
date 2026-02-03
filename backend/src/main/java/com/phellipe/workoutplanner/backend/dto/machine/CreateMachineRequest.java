package com.phellipe.workoutplanner.backend.dto.machine;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateMachineRequest {

    @NotBlank(message = "Machine name is required")
    private String name;
    @NotNull(message = "Machine number is required")
    @Positive(message = "Machine number must be positive")
    private Integer number;

}
