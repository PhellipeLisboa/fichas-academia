package com.phellipe.workoutplanner.backend.service;

import com.phellipe.workoutplanner.backend.domain.entity.Exercise;
import com.phellipe.workoutplanner.backend.domain.repository.ExerciseRepository;
import com.phellipe.workoutplanner.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public List<Exercise> findAllActiveExercises() {
        return exerciseRepository.findByActiveTrue();
    }

    public Exercise FindById(Long id) {
        return getExerciseEntity(id);
    }

    public Exercise getExerciseEntity(Long id) {
        return exerciseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Exercise not found with id: " + id)
        );
    }

}
