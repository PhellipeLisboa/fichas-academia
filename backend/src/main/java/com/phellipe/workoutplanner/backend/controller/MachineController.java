package com.phellipe.workoutplanner.backend.controller;

import com.phellipe.workoutplanner.backend.dto.machine.CreateMachineRequest;
import com.phellipe.workoutplanner.backend.dto.machine.MachineResponse;
import com.phellipe.workoutplanner.backend.dto.machine.MachineSummaryResponse;
import com.phellipe.workoutplanner.backend.dto.machine.UpdateMachineRequest;
import com.phellipe.workoutplanner.backend.service.MachineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/machines")
public class MachineController {

    private final MachineService machineService;

    @PostMapping
    public ResponseEntity<MachineResponse> createMachine(@Valid @RequestBody CreateMachineRequest request) {
        MachineResponse response = machineService.createMachine(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MachineResponse> getMachineById(@PathVariable Long id) {
        MachineResponse response = machineService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<MachineSummaryResponse>> getActiveMachines() {
        List<MachineSummaryResponse> response = machineService.findAllActiveMachines();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MachineResponse>> getAllMachines() {
        List<MachineResponse> response = machineService.findAllMachines();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MachineResponse> updateMachine(@PathVariable Long id, @Valid @RequestBody UpdateMachineRequest request) {
        MachineResponse response = machineService.updateMachine(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/inactivate")
    public ResponseEntity<Void> inactivateMachine(@PathVariable Long id) {
        machineService.inactivateMachine(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateMachine(@PathVariable Long id) {
        machineService.activateMachine(id);
        return ResponseEntity.noContent().build();
    }

}
