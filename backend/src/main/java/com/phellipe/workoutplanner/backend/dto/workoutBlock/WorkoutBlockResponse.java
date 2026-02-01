package com.phellipe.workoutplanner.backend.dto.workoutBlock;

import com.phellipe.workoutplanner.backend.domain.entity.WorkoutBlock;
import com.phellipe.workoutplanner.backend.domain.enumtype.ExecutionType;
import lombok.Data;

@Data
public class WorkoutBlockResponse {

    private Long id;
    private Long workoutId;
    private ExecutionType executionType;
    private Integer position;
    private Integer itemCount;

    public static WorkoutBlockResponse from(WorkoutBlock block) {
        WorkoutBlockResponse response = new WorkoutBlockResponse();
        response.setId(block.getId());
        response.setWorkoutId(block.getWorkout().getId());
        response.setExecutionType(block.getExecutionType());
        response.setPosition(block.getPosition());
        response.setItemCount(block.getItems() != null ? block.getItems().size() : 0);
        return response;
    }
}
