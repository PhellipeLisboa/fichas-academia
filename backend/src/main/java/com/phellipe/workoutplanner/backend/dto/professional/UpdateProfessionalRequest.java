package com.phellipe.workoutplanner.backend.dto.professional;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateProfessionalRequest {

    private String name;

    @Email(message = "Invalid email format")
    private String email;

}
