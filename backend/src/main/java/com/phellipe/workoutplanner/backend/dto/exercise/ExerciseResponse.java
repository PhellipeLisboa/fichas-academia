package com.phellipe.workoutplanner.backend.dto.exercise;


import com.phellipe.workoutplanner.backend.domain.entity.Exercise;
import lombok.Data;

@Data
public class ExerciseResponse {

    private Long id;
    private String name;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
    private Boolean active;

    public static ExerciseResponse from(Exercise exercise) {
        ExerciseResponse response = new ExerciseResponse();
        response.setId(exercise.getId());
        response.setName(exercise.getName());
        response.setDescription(exercise.getDescription());
        response.setVideoUrl(exercise.getVideoUrl());
        response.setThumbnailUrl(exercise.getThumbnailUrl());
        response.setActive(exercise.getActive());
        return response;
    }

}
