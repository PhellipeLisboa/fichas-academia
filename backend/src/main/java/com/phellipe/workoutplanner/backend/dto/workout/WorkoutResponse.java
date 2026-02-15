package com.phellipe.workoutplanner.backend.dto.workout;

import com.phellipe.workoutplanner.backend.domain.entity.Workout;
import com.phellipe.workoutplanner.backend.dto.workoutBlock.WorkoutBlockResponse;
import lombok.Data;

import java.util.List;

@Data
public class WorkoutResponse {

    private Long id;
    private Long workoutPlanId;
    private String name;
    private Integer position;
    private List<WorkoutBlockResponse> blocks;

    public static WorkoutResponse from(Workout workout) {
        WorkoutResponse response = new WorkoutResponse();
        response.setId(workout.getId());
        response.setWorkoutPlanId(workout.getWorkoutPlan().getId());
        response.setName(workout.getName());
        response.setPosition(workout.getPosition());

        if (workout.getBlocks() != null) {
            response.setBlocks(
                    workout.getBlocks().stream()
                            .map(WorkoutBlockResponse::fromWithItems)
                            .toList()
            );
        }

        return response;
    }

    public static WorkoutResponse fromNested(Workout workout) {
        WorkoutResponse response = new WorkoutResponse();
        response.setId(workout.getId());
        response.setWorkoutPlanId(null);
        response.setName(workout.getName());
        response.setPosition(workout.getPosition());

        if (workout.getBlocks() != null) {
            response.setBlocks(
                    workout.getBlocks().stream()
                            .map(WorkoutBlockResponse::fromWithItems)
                            .toList()
            );
        }

        return response;
    }

}
