package com.phellipe.workoutplanner.backend.dto.professional;

import com.phellipe.workoutplanner.backend.domain.entity.Professional;
import lombok.Data;

@Data
public class ProfessionalSummaryResponse {

    private Long id;
    private String name;
    private String email;

    public static ProfessionalSummaryResponse from(Professional professional) {
        ProfessionalSummaryResponse response = new ProfessionalSummaryResponse();
        response.setId(professional.getId());
        response.setName(professional.getName());
        response.setEmail(professional.getEmail());
        return response;
    }

}
