package com.phellipe.workoutplanner.backend.dto.machine;

import com.phellipe.workoutplanner.backend.domain.entity.Machine;
import lombok.Data;

@Data
public class MachineSummaryResponse {

    private Long id;
    private String name;
    private Integer number;

    public static MachineSummaryResponse from(Machine machine) {
        MachineSummaryResponse response = new MachineSummaryResponse();
        response.setId(machine.getId());
        response.setName(machine.getName());
        response.setNumber(machine.getNumber());
        return response;
    }

}
