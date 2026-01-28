package com.phellipe.workoutplanner.backend.domain.entity;

import com.phellipe.workoutplanner.backend.domain.enumtype.Intensity;
import com.phellipe.workoutplanner.backend.domain.enumtype.WorkoutPlanStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "workout_plan")
public class WorkoutPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(optional = false)
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "review_date", nullable = false)
    private LocalDate reviewDate;

    @Column(name = "reassessment_date", nullable = false)
    private LocalDate reassessmentDate;

    @Column(name = "sheet_number")
    private Integer sheetNumber;

    @Column(name = "rest_seconds")
    private Integer restSeconds;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Intensity intensity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkoutPlanStatus status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "workoutPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<Workout> workouts;

}
