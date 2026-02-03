package com.phellipe.workoutplanner.backend.service;

import com.phellipe.workoutplanner.backend.domain.entity.Machine;
import com.phellipe.workoutplanner.backend.domain.repository.MachineRepository;
import com.phellipe.workoutplanner.backend.dto.machine.CreateMachineRequest;
import com.phellipe.workoutplanner.backend.dto.machine.MachineResponse;
import com.phellipe.workoutplanner.backend.dto.machine.MachineSummaryResponse;
import com.phellipe.workoutplanner.backend.dto.machine.UpdateMachineRequest;
import com.phellipe.workoutplanner.backend.exception.InvalidDataException;
import com.phellipe.workoutplanner.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MachineService {

    private final MachineRepository machineRepository;

    @Transactional
    public MachineResponse createMachine(CreateMachineRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new InvalidDataException("Machine name cannot be empty");
        }

        Machine machine = new Machine();
        machine.setName(request.getName().trim().toUpperCase());
        machine.setNumber(request.getNumber());
        machine.setActive(true);

        Machine savedMachine = machineRepository.save(machine);
        return MachineResponse.from(savedMachine);

    }

    @Transactional
    public MachineResponse updateMachine(Long id, UpdateMachineRequest request) {
        Machine machine = getMachineEntity(id);

        if (request.getName() != null && !request.getName().isBlank()) {
            machine.setName(request.getName().trim().toUpperCase());
        }

        if (request.getNumber() != null) {
            machine.setNumber(request.getNumber());
        }

        Machine updatedMachine = machineRepository.save(machine);
        return MachineResponse.from(updatedMachine);
    }

    @Transactional
    public void inactivateMachine(Long id) {
        Machine machine = getMachineEntity(id);
        machine.setActive(false);
        machineRepository.save(machine);
    }

    @Transactional
    public void activateMachine(Long id) {
        Machine machine = getMachineEntity(id);
        machine.setActive(true);
        machineRepository.save(machine);
    }

    public List<MachineSummaryResponse> findAllActiveMachines() {
        return machineRepository.findByActiveTrue()
                .stream()
                .map(MachineSummaryResponse::from)
                .toList();
    }

    public List<MachineResponse> findAllMachines() {
        return machineRepository.findAll()
                .stream()
                .map(MachineResponse::from)
                .toList();
    }

    public MachineResponse FindById(Long id) {
        Machine machine = getMachineEntity(id);
        return MachineResponse.from(machine);
    }

    public Machine getMachineEntity(Long id) {
        return machineRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Machine not found with id: " + id)
        );
    }

}
