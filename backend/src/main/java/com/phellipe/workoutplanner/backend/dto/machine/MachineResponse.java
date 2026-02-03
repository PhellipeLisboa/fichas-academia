package com.phellipe.workoutplanner.backend.dto.machine;

import com.phellipe.workoutplanner.backend.domain.entity.Machine;
import lombok.Data;

@Data
public class MachineResponse {

    private Long id;
    private String name;
    private Integer number;
    private Boolean active;

    public static MachineResponse from(Machine machine) {
        MachineResponse response = new MachineResponse();
        response.setId(machine.getId());
        response.setName(machine.getName());
        response.setNumber(machine.getNumber());
        response.setActive(machine.getActive());
        return response;
    }

}
