package com.phellipe.workoutplanner.backend.domain.entity;

import com.phellipe.workoutplanner.backend.domain.enumtype.ExecutionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "workout_block")
public class WorkoutBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @OneToMany(mappedBy = "workoutBlock", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<BlockItem> items;

    @Enumerated(EnumType.STRING)
    @Column(name = "execution_type", nullable = false)
    private ExecutionType executionType;

    @Column(nullable = false)
    private Integer position;

}
