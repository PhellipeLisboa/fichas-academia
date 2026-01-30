package com.phellipe.workoutplanner.backend.dto.workout;

import com.phellipe.workoutplanner.backend.domain.entity.Workout;
import lombok.Data;

@Data
public class WorkoutResponse {

    private Long id;
    private Long workoutPlanId;
    private String name;
    private Integer position;

    public static WorkoutResponse from(Workout workout) {
        WorkoutResponse response = new WorkoutResponse();
        response.setId(workout.getId());
        response.setWorkoutPlanId(workout.getWorkoutPlan().getId());
        response.setName(workout.getName());
        response.setPosition(workout.getPosition());
        return response;
    }

}
