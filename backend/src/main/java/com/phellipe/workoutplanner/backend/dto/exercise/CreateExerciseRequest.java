package com.phellipe.workoutplanner.backend.dto.exercise;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateExerciseRequest {

    @NotBlank(message = "Exercise name is required")
    private String name;

    private String description;
    private String videoUrl;
    private String thumbnailUrl;
}
