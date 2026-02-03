package com.phellipe.workoutplanner.backend.dto.machine;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateMachineRequest {

    private String name;

    @Positive(message = "Machine number must be positive")
    private Integer number;

}
