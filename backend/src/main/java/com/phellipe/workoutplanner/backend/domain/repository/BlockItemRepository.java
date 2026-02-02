package com.phellipe.workoutplanner.backend.domain.repository;

import com.phellipe.workoutplanner.backend.domain.entity.BlockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockItemRepository extends JpaRepository<BlockItem, Long> {

    List<BlockItem> findByWorkoutBlockIdOrderByPositionAsc(Long workoutItemId);

    @Query("SELECT COALESCE(MAX(i.position), 0) FROM BlockItem i WHERE i.workoutBlock.id = :workoutBlockId")
    Integer findMaxPositionByWorkoutBlockId(Long workoutBlockId);

}
