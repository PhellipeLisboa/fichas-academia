package com.phellipe.workoutplanner.backend.dto.exercise;

import lombok.Data;

@Data
public class UpdateExerciseRequest {

    private String name;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
}
