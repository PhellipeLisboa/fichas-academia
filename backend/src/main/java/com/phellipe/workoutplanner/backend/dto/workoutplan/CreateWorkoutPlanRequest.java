package com.phellipe.workoutplanner.backend.dto.workoutplan;

import com.phellipe.workoutplanner.backend.domain.enumtype.Intensity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateWorkoutPlanRequest {

    @NotNull(message = "Member ID is required")
    private Long memberId;

    @NotNull(message = "Professional ID is required")
    private Long professionalId;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "Review date is required")
    private LocalDate reviewDate;

    @NotNull(message = "Reassessment date is required")
    private LocalDate reassessmentDate;

    @Positive(message = "Sheet number must be positive")
    private Integer sheetNumber;

    @Positive(message = "Rest interval must be positive")
    private Integer restSeconds;

    @NotNull(message = "Intensity is required")
    private Intensity intensity;

    private String notes;

}
