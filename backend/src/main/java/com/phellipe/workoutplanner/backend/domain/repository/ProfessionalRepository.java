package com.phellipe.workoutplanner.backend.domain.repository;

import com.phellipe.workoutplanner.backend.domain.entity.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessionalRepository extends JpaRepository<Professional, Long> {

    Boolean existsByEmail(String email);

    Optional<Professional> findByEmail(String email);

    List<Professional> findByActiveTrue();

}
