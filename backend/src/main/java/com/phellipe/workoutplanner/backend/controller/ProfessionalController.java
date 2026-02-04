package com.phellipe.workoutplanner.backend.controller;

import com.phellipe.workoutplanner.backend.dto.professional.CreateProfessionalRequest;
import com.phellipe.workoutplanner.backend.dto.professional.ProfessionalResponse;
import com.phellipe.workoutplanner.backend.dto.professional.ProfessionalSummaryResponse;
import com.phellipe.workoutplanner.backend.dto.professional.UpdateProfessionalRequest;
import com.phellipe.workoutplanner.backend.service.ProfessionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/professionals")
public class ProfessionalController {

    private final ProfessionalService professionalService;

    @PostMapping
    public ResponseEntity<ProfessionalResponse> createProfessional(@Valid @RequestBody CreateProfessionalRequest request) {
        ProfessionalResponse response = professionalService.createProfessional(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalResponse> getProfessionalById(@PathVariable Long id) {
        ProfessionalResponse response = professionalService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ProfessionalResponse> getProfessionalById(@PathVariable String email) {
        ProfessionalResponse response = professionalService.findByEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ProfessionalSummaryResponse>> getActiveProfessionals() {
        List<ProfessionalSummaryResponse> response = professionalService.findAllActiveProfessionals();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalResponse> updateProfessional(@PathVariable Long id, @Valid @RequestBody UpdateProfessionalRequest request) {
        ProfessionalResponse response = professionalService.updateProfessional(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/inactivate")
    public ResponseEntity<Void> inactivateProfessional(@PathVariable Long id) {
        professionalService.inactivateProfessional(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateProfessional(@PathVariable Long id) {
        professionalService.activateProfessional(id);
        return ResponseEntity.noContent().build();
    }

}
