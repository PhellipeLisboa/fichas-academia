package com.phellipe.workoutplanner.backend.dto.workoutPlan;

import com.phellipe.workoutplanner.backend.domain.enumtype.Intensity;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateWorkoutPlanRequest {

    private LocalDate reviewDate;
    private LocalDate reassessmentDate;

    @Positive(message = "Sheet number must be positive")
    private Integer sheetNumber;

    @Positive(message = "Rest interval must be positive")
    private Integer restSeconds;

    private Intensity intensity;

    private String notes;

}
