package com.phellipe.workoutplanner.backend.service;

import com.phellipe.workoutplanner.backend.domain.entity.Exercise;
import com.phellipe.workoutplanner.backend.domain.repository.ExerciseRepository;
import com.phellipe.workoutplanner.backend.dto.exercise.CreateExerciseRequest;
import com.phellipe.workoutplanner.backend.dto.exercise.ExerciseResponse;
import com.phellipe.workoutplanner.backend.dto.exercise.ExerciseSummaryResponse;
import com.phellipe.workoutplanner.backend.dto.exercise.UpdateExerciseRequest;
import com.phellipe.workoutplanner.backend.exception.InvalidDataException;
import com.phellipe.workoutplanner.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    @Transactional
    public ExerciseResponse createExercise(CreateExerciseRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new InvalidDataException("Exercise name cannot be empty");
        }

        Exercise exercise = new Exercise();
        exercise.setName(request.getName().trim().toUpperCase());
        exercise.setDescription(request.getDescription().trim().toUpperCase());
        exercise.setVideoUrl(request.getVideoUrl().trim());
        exercise.setThumbnailUrl(request.getThumbnailUrl());
        exercise.setActive(true);

        Exercise savedExercise = exerciseRepository.save(exercise);
        return ExerciseResponse.from(savedExercise);

    }

    @Transactional
    public ExerciseResponse updateExercise(Long id, UpdateExerciseRequest request) {
        Exercise exercise = getExerciseEntity(id);

        if (request.getName() != null && !request.getName().isBlank()) {
            exercise.setName(request.getName().trim().toUpperCase());
        }

        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            exercise.setDescription(request.getDescription().trim().toUpperCase());
        }

        if (request.getVideoUrl() != null) {
            exercise.setVideoUrl(exercise.getVideoUrl().trim());
        }

        if (request.getThumbnailUrl() != null) {
            exercise.setThumbnailUrl(exercise.getThumbnailUrl().trim());
        }

        Exercise updatedExercise = exerciseRepository.save(exercise);
        return ExerciseResponse.from(updatedExercise);
    }

    @Transactional
    public void inactivateExercise(Long id) {
        Exercise exercise = getExerciseEntity(id);
        exercise.setActive(false);
        exerciseRepository.save(exercise);
    }

    @Transactional
    public void activateExercise(Long id) {
        Exercise exercise = getExerciseEntity(id);
        exercise.setActive(true);
        exerciseRepository.save(exercise);
    }

    public List<ExerciseSummaryResponse> findAllActiveExercises() {
        return exerciseRepository.findByActiveTrue()
                .stream()
                .map(ExerciseSummaryResponse::from)
                .toList();
    }

    public List<ExerciseResponse> findAllExercises() {
        return exerciseRepository.findAll()
                .stream()
                .map(ExerciseResponse::from)
                .toList();
    }

    public ExerciseResponse FindById(Long id) {
        Exercise exercise = getExerciseEntity(id);
        return ExerciseResponse.from(exercise);
    }

    public Exercise getExerciseEntity(Long id) {
        return exerciseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Exercise not found with id: " + id)
        );
    }

}
