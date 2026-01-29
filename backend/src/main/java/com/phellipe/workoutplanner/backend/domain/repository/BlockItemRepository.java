package com.phellipe.workoutplanner.backend.domain.repository;

import com.phellipe.workoutplanner.backend.domain.entity.BlockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockItemRepository extends JpaRepository<BlockItem, Long> {
}
