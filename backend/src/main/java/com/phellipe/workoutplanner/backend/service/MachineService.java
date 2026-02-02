package com.phellipe.workoutplanner.backend.service;

import com.phellipe.workoutplanner.backend.domain.entity.Machine;
import com.phellipe.workoutplanner.backend.domain.repository.MachineRepository;
import com.phellipe.workoutplanner.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MachineService {

    private final MachineRepository machineRepository;

    public List<Machine> findAllActiveMachines() {
        return machineRepository.findByActiveTrue();
    }

    public Machine FindById(Long id) {
        return getMachineEntity(id);
    }

    public Machine getMachineEntity(Long id) {
        return machineRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Machine not found with id: " + id)
        );
    }

}
