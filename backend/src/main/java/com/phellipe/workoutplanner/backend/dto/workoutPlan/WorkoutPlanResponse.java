package com.phellipe.workoutplanner.backend.dto.workoutPlan;

import com.phellipe.workoutplanner.backend.domain.entity.WorkoutPlan;
import com.phellipe.workoutplanner.backend.domain.enumtype.Intensity;
import com.phellipe.workoutplanner.backend.domain.enumtype.WorkoutPlanStatus;
import com.phellipe.workoutplanner.backend.dto.workout.WorkoutResponse;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class WorkoutPlanResponse {

    private Long id;
    private Long memberId;
    private String memberName;
    private Long professionalId;
    private String professionalName;
    private LocalDate startDate;
    private LocalDate reviewDate;
    private LocalDate reassessmentDate;
    private Integer sheetNumber;
    private Integer restSeconds;
    private Intensity intensity;
    private WorkoutPlanStatus status;
    private String notes;
    private List<WorkoutResponse> workouts;

    public static WorkoutPlanResponse from(WorkoutPlan plan) {
        WorkoutPlanResponse response = new WorkoutPlanResponse();
        response.setId(plan.getId());
        response.setMemberId(plan.getMember().getId());
        response.setMemberName(plan.getMember().getName());
        response.setProfessionalId(plan.getProfessional().getId());
        response.setProfessionalName(plan.getProfessional().getName());
        response.setStartDate(plan.getStartDate());
        response.setReviewDate(plan.getReviewDate());
        response.setReassessmentDate(plan.getReassessmentDate());
        response.setSheetNumber(plan.getSheetNumber());
        response.setRestSeconds(plan.getRestSeconds());
        response.setIntensity(plan.getIntensity());
        response.setStatus(plan.getStatus());
        response.setNotes(plan.getNotes());
        return response;
    }

    public static WorkoutPlanResponse fromWithWorkouts(WorkoutPlan plan) {
        WorkoutPlanResponse response = from(plan);

        if (plan.getWorkouts() != null && !plan.getWorkouts().isEmpty()) {

            List<WorkoutResponse> workoutResponses = plan.getWorkouts().stream()
                    .map(w -> {
                        WorkoutResponse wr = WorkoutResponse.fromNested(w);
                        return wr;
                    })
                    .toList();

            response.setWorkouts(workoutResponses);
        }

        return response;
    }

}
