package com.phellipe.workoutplanner.backend.dto.workoutBlock;

import com.phellipe.workoutplanner.backend.domain.entity.WorkoutBlock;
import com.phellipe.workoutplanner.backend.domain.enumtype.ExecutionType;
import com.phellipe.workoutplanner.backend.dto.blockItem.BlockItemResponse;
import lombok.Data;

import java.util.List;

@Data
public class WorkoutBlockResponse {

    private Long id;
    private Long workoutId;
    private ExecutionType executionType;
    private Integer position;
    private Integer itemCount;
    private List<BlockItemResponse> items;

    public static WorkoutBlockResponse from(WorkoutBlock block) {
        WorkoutBlockResponse response = new WorkoutBlockResponse();
        response.setId(block.getId());
        response.setWorkoutId(block.getWorkout().getId());
        response.setExecutionType(block.getExecutionType());
        response.setPosition(block.getPosition());
        response.setItemCount(block.getItems() != null ? block.getItems().size() : 0);
        return response;
    }

    public static WorkoutBlockResponse fromWithItems(WorkoutBlock block) {
        WorkoutBlockResponse response = from(block);

        if (block.getItems() != null) {
            response.setItems(
                    block.getItems().stream()
                            .map(BlockItemResponse::from)
                            .toList()
            );
        }

        return response;
    }

}
