package com.phellipe.workoutplanner.backend.service;

import com.phellipe.workoutplanner.backend.domain.entity.WorkoutPlan;
import com.phellipe.workoutplanner.backend.service.pdf.WorkoutPlanPdfGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final WorkoutPlanPdfGenerator pdfGenerator;
    private final WorkoutPlanService workoutPlanService;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public byte[] generateWorkoutPlanPdf(Long workoutPlanId) {
        WorkoutPlan workoutPlan = workoutPlanService.getWorkoutPlanEntity(workoutPlanId);

        String qrCodeUrl = baseUrl + "/api/workout-plans/public/" + workoutPlan.getMember().getPublicCode();

        return pdfGenerator.generateWorkoutPlanPdf(workoutPlan, qrCodeUrl);

    }

}
