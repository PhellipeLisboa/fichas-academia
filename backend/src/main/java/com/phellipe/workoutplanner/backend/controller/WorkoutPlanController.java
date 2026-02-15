package com.phellipe.workoutplanner.backend.controller;

import com.phellipe.workoutplanner.backend.dto.workoutPlan.CreateWorkoutPlanRequest;
import com.phellipe.workoutplanner.backend.dto.workoutPlan.UpdateWorkoutPlanRequest;
import com.phellipe.workoutplanner.backend.dto.workoutPlan.WorkoutPlanResponse;
import com.phellipe.workoutplanner.backend.dto.workoutPlan.WorkoutPlanSummaryResponse;
import com.phellipe.workoutplanner.backend.service.PdfService;
import com.phellipe.workoutplanner.backend.service.WorkoutPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workout-plans")
@RequiredArgsConstructor
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;
    private final PdfService pdfService;

    @PostMapping
    public ResponseEntity<WorkoutPlanResponse> createWorkoutPlan(@Valid @RequestBody CreateWorkoutPlanRequest request) {
        WorkoutPlanResponse response = workoutPlanService.createWorkoutPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutPlanResponse> updateWorkoutPlan(@PathVariable Long id, @Valid @RequestBody UpdateWorkoutPlanRequest request) {
        WorkoutPlanResponse response = workoutPlanService.updateWorkoutPlan(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/finalize")
    public ResponseEntity<WorkoutPlanResponse> finalizeWorkoutPlan(@PathVariable Long id) {
        WorkoutPlanResponse response = workoutPlanService.finalizeWorkoutPlan(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reassessment")
    public ResponseEntity<WorkoutPlanResponse> createReassessmentPlan(@Valid @RequestBody CreateWorkoutPlanRequest request) {
        WorkoutPlanResponse response = workoutPlanService.createReassessmentPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutPlanResponse> getWorkoutPlanById(@PathVariable Long id) {
        WorkoutPlanResponse response = workoutPlanService.findWorkoutPlanById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/member/{memberId}/active")
    public ResponseEntity<WorkoutPlanResponse> getActivePlanByMember(@PathVariable Long memberId) {
        WorkoutPlanResponse response = workoutPlanService.findActivePlanByMemberId(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/{publicCode}")
    @Transactional
    public ResponseEntity<WorkoutPlanResponse> getActivePlanByPublicCode(@PathVariable String publicCode) {
        WorkoutPlanResponse response = workoutPlanService.findActivePlanByPublicCode(publicCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<WorkoutPlanSummaryResponse>> getPlansByMember(@PathVariable Long memberId) {
        List<WorkoutPlanSummaryResponse> response = workoutPlanService.findAllByMemberId(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/professional/{professionalId}")
    public ResponseEntity<List<WorkoutPlanSummaryResponse>> getPlansByProfessional(@PathVariable Long professionalId) {
        List<WorkoutPlanSummaryResponse> response = workoutPlanService.findAllByProfessionalId(professionalId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        byte[] pdfBytes = pdfService.generateWorkoutPlanPdf(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(
                "attachment", "ficha-treino-" + workoutPlanService.getWorkoutPlanEntity(id).getMember().getName()
                        + "-" + workoutPlanService.getWorkoutPlanEntity(id).getSheetNumber() + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

}
