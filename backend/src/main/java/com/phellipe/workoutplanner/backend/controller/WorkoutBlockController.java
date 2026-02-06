package com.phellipe.workoutplanner.backend.controller;

import com.phellipe.workoutplanner.backend.dto.workoutBlock.CreateWorkoutBlockRequest;
import com.phellipe.workoutplanner.backend.dto.workoutBlock.ReorderWorkoutBlocksRequest;
import com.phellipe.workoutplanner.backend.dto.workoutBlock.UpdateWorkoutBlockRequest;
import com.phellipe.workoutplanner.backend.dto.workoutBlock.WorkoutBlockResponse;
import com.phellipe.workoutplanner.backend.service.WorkoutBlockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workout-blocks")
public class WorkoutBlockController {

    private final WorkoutBlockService workoutBlockService;

    @PostMapping
    public ResponseEntity<WorkoutBlockResponse> createWorkoutBlock(@Valid @RequestBody CreateWorkoutBlockRequest request) {
        WorkoutBlockResponse response = workoutBlockService.createWorkoutBlock(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutBlockResponse> updateExecutionType(@PathVariable Long id, @Valid @RequestBody UpdateWorkoutBlockRequest request) {
        WorkoutBlockResponse response = workoutBlockService.updateExecutionType(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/workout/{workoutId}/reorder")
    public ResponseEntity<List<WorkoutBlockResponse>> reorderBlocks(@PathVariable Long workoutId, @Valid @RequestBody ReorderWorkoutBlocksRequest request) {
        List<WorkoutBlockResponse> response = workoutBlockService.reorderBlocks(workoutId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkoutBlock(@PathVariable Long id) {
        workoutBlockService.deleteWorkoutBlock(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutBlockResponse> getWorkoutBlockById(@PathVariable Long id) {
        WorkoutBlockResponse response = workoutBlockService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/workout/{workoutId}")
    public ResponseEntity<List<WorkoutBlockResponse>> getBlocksByWorkout(@PathVariable Long workoutId) {
        List<WorkoutBlockResponse> response = workoutBlockService.findAllByWorkoutId(workoutId);
        return ResponseEntity.ok(response);
    }


}
