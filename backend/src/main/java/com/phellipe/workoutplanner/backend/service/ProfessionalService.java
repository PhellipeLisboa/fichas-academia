package com.phellipe.workoutplanner.backend.service;

import com.phellipe.workoutplanner.backend.domain.entity.Professional;
import com.phellipe.workoutplanner.backend.domain.repository.ProfessionalRepository;
import com.phellipe.workoutplanner.backend.dto.professional.CreateProfessionalRequest;
import com.phellipe.workoutplanner.backend.dto.professional.ProfessionalResponse;
import com.phellipe.workoutplanner.backend.dto.professional.ProfessionalSummaryResponse;
import com.phellipe.workoutplanner.backend.dto.professional.UpdateProfessionalRequest;
import com.phellipe.workoutplanner.backend.exception.BusinessException;
import com.phellipe.workoutplanner.backend.exception.InvalidDataException;
import com.phellipe.workoutplanner.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;

    @Transactional
    public ProfessionalResponse createProfessional(CreateProfessionalRequest request) {

        validateName(request.getName());
        validateEmail(request.getEmail());

        String normalizedEmail = request.getEmail().trim().toLowerCase();

        if (professionalRepository.existsByEmail(normalizedEmail)) {
            throw new BusinessException("Professional with email " + normalizedEmail + " already exists");
        }

        Professional professional = new Professional();
        professional.setName(request.getName());
        professional.setEmail(request.getEmail());
        professional.setActive(true);

        return ProfessionalResponse.from(professionalRepository.save(professional));

    }

    public ProfessionalResponse findById(Long id) {
        Professional professional =  professionalRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Professional not found with id: " + id));

        return ProfessionalResponse.from(professional);
    }

    public ProfessionalResponse findByEmail(String email) {
        Professional professional = professionalRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Professional not found with email: " + email));

        return ProfessionalResponse.from(professional);
    }

    public List<ProfessionalSummaryResponse> findAllActiveProfessionals() {
        return professionalRepository.findByActiveTrue()
                .stream()
                .map(ProfessionalSummaryResponse::from)
                .toList();
    }

    @Transactional
    public ProfessionalResponse updateProfessional(Long id, UpdateProfessionalRequest request) {
        Professional professional = getProfessionalEntity(id);

        if (request.getName() != null && !request.getName().isBlank()) {
            validateName(request.getName());
            professional.setName(request.getName().trim());
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            validateEmail(request.getEmail());
            String normalizedEmail = request.getEmail().trim().toLowerCase();

            if (!professional.getEmail().equals(normalizedEmail) && professionalRepository.existsByEmail(normalizedEmail)) {
                throw new BusinessException("Professional with email " + normalizedEmail + " already exists");
            }

            professional.setEmail(normalizedEmail);

        }

        Professional updatedProfessional = professionalRepository.save(professional);
        return ProfessionalResponse.from(professional);

    }

    @Transactional
    public void inactivateProfessional(Long id) {
        Professional professional = getProfessionalEntity(id);
        professional.setActive(false);
        professionalRepository.save(professional);
    }

    @Transactional
    public void activateProfessional(Long id) {
        Professional professional = getProfessionalEntity(id);
        professional.setActive(true);
        professionalRepository.save(professional);
    }

    public Professional getProfessionalEntity(Long id) {
        return professionalRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Professional not found with id: " + id));
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidDataException("Professional name cannot be empty");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidDataException("Professional email cannot be empty");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new InvalidDataException("Invalid email format");
        }
    }


}
