package com.phellipe.workoutplanner.backend.service;

import com.phellipe.workoutplanner.backend.domain.entity.Workout;
import com.phellipe.workoutplanner.backend.domain.entity.WorkoutBlock;
import com.phellipe.workoutplanner.backend.domain.enumtype.WorkoutPlanStatus;
import com.phellipe.workoutplanner.backend.domain.repository.WorkoutBlockRepository;
import com.phellipe.workoutplanner.backend.dto.workoutBlock.CreateWorkoutBlockRequest;
import com.phellipe.workoutplanner.backend.dto.workoutBlock.ReorderWorkoutBlocksRequest;
import com.phellipe.workoutplanner.backend.dto.workoutBlock.UpdateWorkoutBlockRequest;
import com.phellipe.workoutplanner.backend.dto.workoutBlock.WorkoutBlockResponse;
import com.phellipe.workoutplanner.backend.exception.BusinessException;
import com.phellipe.workoutplanner.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutBlockService {

    private final WorkoutBlockRepository workoutBlockRepository;
    private final WorkoutService workoutService;

    @Transactional
    public WorkoutBlockResponse createWorkoutBlock(CreateWorkoutBlockRequest request) {
        Workout workout = workoutService.getWorkoutEntity(request.getWorkoutId());

        if (workout.getWorkoutPlan().getStatus() != WorkoutPlanStatus.DRAFT) {
            throw new BusinessException("Cannot add block to finalized plan");
        }

        Integer maxPosition = workoutBlockRepository.findMaxPositionByWorkoutId(workout.getId());
        Integer nextPosition = maxPosition + 1;

        WorkoutBlock block = new WorkoutBlock();
        block.setWorkout(workout);
        block.setExecutionType(request.getExecutionType());
        block.setPosition(nextPosition);

        WorkoutBlock savedBlock = workoutBlockRepository.save(block);
        return WorkoutBlockResponse.from(savedBlock);

    }

    @Transactional
    public WorkoutBlockResponse updateExecutionType(Long blockId, UpdateWorkoutBlockRequest request) {
        WorkoutBlock block = getWorkoutBlockEntity(blockId);

        if (block.getWorkout().getWorkoutPlan().getStatus() == WorkoutPlanStatus.INACTIVE) {
            throw new BusinessException("Cannot update block in inactive plan");
        }

        block.setExecutionType(request.getExecutionType());

        WorkoutBlock updatedBlock = workoutBlockRepository.save(block);
        return WorkoutBlockResponse.from(updatedBlock);

    }

    @Transactional
    public List<WorkoutBlockResponse> reorderBlocks(Long workoutId, ReorderWorkoutBlocksRequest request) {
        Workout workout = workoutService.getWorkoutEntity(workoutId);

        if (workout.getWorkoutPlan().getStatus() == WorkoutPlanStatus.INACTIVE) {
            throw new BusinessException("Cannot reorder blocks in inactive plan");
        }

        List<WorkoutBlock> blocks = workoutBlockRepository.findByWorkoutIdOrderByPositionAsc(workoutId);

        if (blocks.size() != request.getBlockIds().size()) {
            throw new BusinessException("Block IDs count mismatch");
        }

        for (Long blockId : request.getBlockIds()) {
            boolean found = blocks
                    .stream()
                    .anyMatch(b -> b.getId().equals(blockId));

            if (!found) {
                throw new BusinessException("Block ID " + blockId + " does not belong this workout");
            }
        }

        for (int i = 0; i < request.getBlockIds().size(); i++) {
            Long blockId = request.getBlockIds().get(i);

            WorkoutBlock block = blocks.stream()
                    .filter(b -> b.getId().equals(blockId))
                    .findFirst()
                    .orElseThrow();

            block.setPosition(i + i);
        }

        List<WorkoutBlock> reorderedBlocks = workoutBlockRepository.saveAll(blocks);
        return reorderedBlocks
                .stream()
                .map(WorkoutBlockResponse::from)
                .toList();

    }

    @Transactional
    public void deleteWorkoutBlock(Long blockId) {

        WorkoutBlock block = getWorkoutBlockEntity(blockId);

        if (block.getWorkout().getWorkoutPlan().getStatus() != WorkoutPlanStatus.DRAFT) {
            throw new BusinessException("Cannot delete block from finalized plan");
        }

        if (block.getItems() != null && !block.getItems().isEmpty()) {
            throw new BusinessException("Cannot delete block with items. Remove all items first.");
        }

        workoutBlockRepository.delete(block);

        List<WorkoutBlock> remainingBlocks = workoutBlockRepository.findByWorkoutIdOrderByPositionAsc(block.getWorkout().getId());

        for (int i = 0; i < remainingBlocks.size(); i++) {
            remainingBlocks.get(i).setPosition(i + 1);
        }

        workoutBlockRepository.saveAll(remainingBlocks);

    }

    public List<WorkoutBlockResponse> findAllByWorkoutId(Long workoutId) {
        return workoutBlockRepository.findByWorkoutIdOrderByPositionAsc(workoutId)
                .stream()
                .map(WorkoutBlockResponse::from)
                .toList();
    }

    public WorkoutBlockResponse findById(Long blockId) {
        WorkoutBlock block = getWorkoutBlockEntity(blockId);
        return WorkoutBlockResponse.from(block);
    }

    public WorkoutBlock getWorkoutBlockEntity(Long id) {
        return workoutBlockRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Workout block not found with id: " + id)
        );
    }

}
