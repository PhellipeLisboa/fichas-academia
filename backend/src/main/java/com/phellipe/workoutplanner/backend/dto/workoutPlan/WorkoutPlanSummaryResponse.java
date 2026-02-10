package com.phellipe.workoutplanner.backend.dto.workoutPlan;

import com.phellipe.workoutplanner.backend.domain.entity.WorkoutPlan;
import com.phellipe.workoutplanner.backend.domain.enumtype.Intensity;
import com.phellipe.workoutplanner.backend.domain.enumtype.WorkoutPlanStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class WorkoutPlanSummaryResponse {

    private Long id;
    private String memberName;
    private String professionalName;
    private LocalDate startDate;
    private Integer sheetNumber;
    private Intensity intensity;
    private WorkoutPlanStatus status;

    public static WorkoutPlanSummaryResponse from(WorkoutPlan plan) {
        WorkoutPlanSummaryResponse response = new WorkoutPlanSummaryResponse();
        response.setId(plan.getId());
        response.setMemberName(plan.getMember().getName());
        response.setProfessionalName(plan.getProfessional().getName());
        response.setStartDate(plan.getStartDate());
        response.setSheetNumber(plan.getSheetNumber());
        response.setIntensity(plan.getIntensity());
        response.setStatus(plan.getStatus());
        return response;
    }

}
