package com.phellipe.workoutplanner.backend.controller;

import com.phellipe.workoutplanner.backend.dto.exercise.CreateExerciseRequest;
import com.phellipe.workoutplanner.backend.dto.exercise.ExerciseResponse;
import com.phellipe.workoutplanner.backend.dto.exercise.ExerciseSummaryResponse;
import com.phellipe.workoutplanner.backend.dto.exercise.UpdateExerciseRequest;
import com.phellipe.workoutplanner.backend.service.ExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @PostMapping
    public ResponseEntity<ExerciseResponse> createExercise(@Valid @RequestBody CreateExerciseRequest request) {
        ExerciseResponse response = exerciseService.createExercise(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponse> getExerciseById(@PathVariable Long id) {
        ExerciseResponse response = exerciseService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ExerciseSummaryResponse>> getActiveExercises() {
        List<ExerciseSummaryResponse> response = exerciseService.findAllActiveExercises();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ExerciseResponse>> getAllExercises() {
        List<ExerciseResponse> response = exerciseService.findAllExercises();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseResponse> updateExercise(@PathVariable Long id, @Valid @RequestBody UpdateExerciseRequest request) {
        ExerciseResponse response = exerciseService.updateExercise(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/inactivate")
    public ResponseEntity<Void> inactivateExercise(@PathVariable Long id) {
        exerciseService.inactivateExercise(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateExercise(@PathVariable Long id) {
        exerciseService.activateExercise(id);
        return ResponseEntity.noContent().build();
    }

}
