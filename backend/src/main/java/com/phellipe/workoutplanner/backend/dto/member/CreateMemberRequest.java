package com.phellipe.workoutplanner.backend.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateMemberRequest {

    @NotBlank(message = "Name is required")
    private String name;

}
