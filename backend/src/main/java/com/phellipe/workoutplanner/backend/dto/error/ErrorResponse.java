package com.phellipe.workoutplanner.backend.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private LocalDateTime time;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
