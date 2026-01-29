package com.phellipe.workoutplanner.backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "block_item")
public class BlockItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "workout_block_id", nullable = false)
    private WorkoutBlock workoutBlock;

    @ManyToOne(optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @ManyToOne(optional = false)
    @JoinColumn(name = "machine_id", nullable = false)
    private Machine machine;

    @Column(nullable = false)
    private Integer sets;

    @Column(nullable = false)
    private Integer reps;

    @Column(nullable = false)
    private Integer position;


}
