package com.phellipe.workoutplanner.backend.domain.repository;

import com.phellipe.workoutplanner.backend.domain.entity.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionalRepository extends JpaRepository<Professional, Long> {
}
