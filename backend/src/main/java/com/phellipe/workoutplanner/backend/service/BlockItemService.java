package com.phellipe.workoutplanner.backend.service;

import com.phellipe.workoutplanner.backend.domain.entity.BlockItem;
import com.phellipe.workoutplanner.backend.domain.entity.Exercise;
import com.phellipe.workoutplanner.backend.domain.entity.Machine;
import com.phellipe.workoutplanner.backend.domain.entity.WorkoutBlock;
import com.phellipe.workoutplanner.backend.domain.enumtype.WorkoutPlanStatus;
import com.phellipe.workoutplanner.backend.domain.repository.BlockItemRepository;
import com.phellipe.workoutplanner.backend.dto.blockItem.*;
import com.phellipe.workoutplanner.backend.exception.BusinessException;
import com.phellipe.workoutplanner.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockItemService {

    private final BlockItemRepository blockItemRepository;
    private final WorkoutBlockService workoutBlockService;
    private final ExerciseService exerciseService;
    private final MachineService machineService;

    @Transactional
    public BlockItemResponse createBlockItem(CreateBlockItemRequest request) {
        WorkoutBlock block = workoutBlockService.getWorkoutBlockEntity(request.getWorkoutBlockId());
        Exercise exercise = exerciseService.getExerciseEntity(request.getExerciseId());
        Machine machine = machineService.getMachineEntity(request.getMachineId());

        Integer maxPosition = blockItemRepository.findMaxPositionByWorkoutBlockId(block.getId());
        Integer nextPosition = maxPosition + 1;

        BlockItem item = new BlockItem();
        item.setWorkoutBlock(block);
        item.setExercise(exercise);
        item.setMachine(machine);
        item.setSets(request.getSets());
        item.setReps(request.getReps());
        item.setPosition(nextPosition);

        BlockItem savedItem = blockItemRepository.save(item);
        return BlockItemResponse.from(savedItem);
    }

    @Transactional
    public BlockItemResponse updateBlockItem(Long itemId, UpdateBlockItemRequest request) {
        BlockItem item = getBlockItemEntity(itemId);

        if (item.getWorkoutBlock().getWorkout().getWorkoutPlan().getStatus() == WorkoutPlanStatus.INACTIVE) {
            throw new BusinessException("Cannot update item in iactive plan");
        }

        if (request.getExerciseId() != null) {
            Exercise exercise = exerciseService.getExerciseEntity(request.getExerciseId());
            item.setExercise(exercise);
        }

        if (request.getMachineId() != null) {
            Machine machine = machineService.getMachineEntity(request.getMachineId());
            item.setMachine(machine);
        }

        if (request.getSets() != null) {
            item.setSets(request.getSets());
        }

        if (request.getReps() != null) {
            item.setReps(request.getReps());
        }

        BlockItem updatedItem = blockItemRepository.save(item);
        return BlockItemResponse.from(updatedItem);

    }

    @Transactional
    public List<BlockItemResponse> reorderItems(Long blockId, ReorderBlockItemsRequest request) {
        WorkoutBlock block = workoutBlockService.getWorkoutBlockEntity(blockId);

        if (block.getWorkout().getWorkoutPlan().getStatus() == WorkoutPlanStatus.INACTIVE) {
            throw new BusinessException("Cannot reorder items in inactive plan");
        }

        List<BlockItem> items = blockItemRepository.findByWorkoutBlockIdOrderByPositionAsc(blockId);

        if (items.size() != request.getItemIds().size()) {
            throw new BusinessException("Item IDs count mismatch");
        }

        for (Long itemId : request.getItemIds()) {
            boolean found = items
                    .stream()
                    .anyMatch(i -> i.getId().equals(itemId));
            if (!found) {
                throw new BusinessException("Item ID " + itemId + " does not belong to this block");
            }
        }

        for (int i = 0; i < request.getItemIds().size(); i++) {
            Long itemId = request.getItemIds().get(i);

            BlockItem item = items.stream()
                    .filter(it -> it.getId().equals(itemId))
                    .findAny()
                    .orElseThrow();

            item.setPosition(i + 1);
        }

        List<BlockItem> reorderedItems = blockItemRepository.saveAll(items);

        return reorderedItems.stream()
                .map(BlockItemResponse::from)
                .toList();

    }

    @Transactional
    public void deleteBlockItem(Long itemId) {
        BlockItem item = getBlockItemEntity(itemId);

        if (item.getWorkoutBlock().getWorkout().getWorkoutPlan().getStatus() == WorkoutPlanStatus.INACTIVE) {
            throw new BusinessException("Cannot delete item from inactive plan");
        }

        WorkoutBlock block = item.getWorkoutBlock();

        blockItemRepository.delete(item);

        List<BlockItem> remainingItems = blockItemRepository.findByWorkoutBlockIdOrderByPositionAsc(block.getId());

        for (int i = 0; i < remainingItems.size(); i++) {
            remainingItems.get(i).setPosition(i + 1);
        }

        blockItemRepository.saveAll(remainingItems);

    }

    public List<BlockItemResponse> findAllByBlockId(Long blockId) {
        return blockItemRepository.findByWorkoutBlockIdOrderByPositionAsc(blockId)
                .stream()
                .map(BlockItemResponse::from)
                .toList();
    }

    public BlockItemResponse findById(Long itemId) {
        BlockItem item = getBlockItemEntity(itemId);
        return BlockItemResponse.from(item);
    }

    public BlockItem getBlockItemEntity(Long id) {
        return blockItemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Block item not found with id: " + id)
        );
    }

}
