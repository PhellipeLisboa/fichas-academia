package com.phellipe.workoutplanner.backend.dto.professional;

import com.phellipe.workoutplanner.backend.domain.entity.Professional;
import lombok.Data;

@Data
public class ProfessionalResponse {

    private Long id;
    private String name;
    private String email;
    private Boolean active;

    public static ProfessionalResponse from(Professional professional) {
        ProfessionalResponse response = new ProfessionalResponse();
        response.setId(professional.getId());
        response.setName(professional.getName());
        response.setEmail(professional.getEmail());
        response.setActive(professional.getActive());
        return response;
    }

}


