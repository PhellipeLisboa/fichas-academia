package com.phellipe.workoutplanner.backend.dto.exercise;

import com.phellipe.workoutplanner.backend.domain.entity.Exercise;
import lombok.Data;

@Data
public class ExerciseSummaryResponse {

    private Long id;
    private String name;
    private String thumbnailUrl;

    public static ExerciseSummaryResponse from(Exercise exercise) {
        ExerciseSummaryResponse response = new ExerciseSummaryResponse();
        response.setId(exercise.getId());
        response.setName(exercise.getName());
        response.setThumbnailUrl(exercise.getThumbnailUrl());
        return response;
    }

}
