package com.phellipe.workoutplanner.backend.dto.blockItem;

import com.phellipe.workoutplanner.backend.domain.entity.BlockItem;
import lombok.Data;

@Data
public class BlockItemResponse {

    private Long id;
    private Long workoutBlockId;
    private Long exerciseId;
    private String exerciseName;
    private Long machineId;
    private String machineName;
    private Integer sets;
    private Integer reps;
    private Integer position;

    public static BlockItemResponse from(BlockItem item) {
        BlockItemResponse response = new BlockItemResponse();
        response.setId(item.getId());
        response.setWorkoutBlockId(item.getWorkoutBlock().getId());
        response.setExerciseId(item.getExercise().getId());
        response.setExerciseName(item.getExercise().getName());
        response.setMachineId(item.getMachine().getId());
        response.setMachineName(item.getMachine().getName());
        response.setSets(item.getSets());
        response.setReps(item.getReps());
        response.setPosition(item.getPosition());
        return response;
    }

}
