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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProfessionalService Tests")
public class ProfessionalServiceTest {

    @Mock
    private ProfessionalRepository professionalRepository;

    @InjectMocks
    private ProfessionalService professionalService;

    private Professional professional;
    private CreateProfessionalRequest createRequest;
    private UpdateProfessionalRequest updateRequest;

    @BeforeEach
    void setUp() {
        professional = new Professional();

        professional.setId(1L);
        professional.setName("Marcos");
        professional.setEmail("marcos@email.com");
        professional.setActive(true);

        createRequest = new CreateProfessionalRequest();
        createRequest.setName("Marcos");
        createRequest.setEmail("marcos@email.com");

        updateRequest = new UpdateProfessionalRequest();

    }

    @Test
    @DisplayName("Should create professional successfully")
    void shouldCreateProfessionalSuccessfully() {

        when(professionalRepository.existsByEmail(anyString())).thenReturn(false);
        when(professionalRepository.save(any(Professional.class))).thenReturn(professional);

        ProfessionalResponse response = professionalService.createProfessional(createRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Marcos");
        assertThat(response.getEmail()).isEqualTo("marcos@email.com");
        assertThat(response.getActive()).isTrue();

        verify(professionalRepository, times(1)).save(any(Professional.class));

    }

    @Test
    @DisplayName("Should throw exception when name is blank")
    void shouldThrowExceptionWhenNameIsBlank() {
        CreateProfessionalRequest blankNameRequest = new CreateProfessionalRequest();
        blankNameRequest.setName("");

        assertThatThrownBy(() -> professionalService.createProfessional(blankNameRequest))
                .isInstanceOf(InvalidDataException.class)
                .hasMessage("Professional name cannot be empty");

        verify(professionalRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when email is empty")
    void shouldThrowExceptionWhenEmailIsEmpty() {
        CreateProfessionalRequest emptyEmailRequest = new CreateProfessionalRequest();
        emptyEmailRequest.setName("Marcos");
        emptyEmailRequest.setEmail("");

        assertThatThrownBy(() -> professionalService.createProfessional(emptyEmailRequest))
                .isInstanceOf(InvalidDataException.class)
                .hasMessage("Professional email cannot be empty");

        verify(professionalRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when email is invalid")
    void shouldThrowExceptionWhenEmailIsInvalid() {
        CreateProfessionalRequest invalidEmailRequest = new CreateProfessionalRequest();
        invalidEmailRequest.setName("Marcos");
        invalidEmailRequest.setEmail("marcos@email");

        assertThatThrownBy(() -> professionalService.createProfessional(invalidEmailRequest))
                .isInstanceOf(InvalidDataException.class)
                .hasMessage("Invalid email format");

        verify(professionalRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        when(professionalRepository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> professionalService.createProfessional(createRequest))
                .isInstanceOf(BusinessException.class);

        verify(professionalRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find professional by id")
    void shouldFindProfessionalById() {
        when(professionalRepository.findById(1L)).thenReturn(Optional.of(professional));

        ProfessionalResponse response = professionalService.findById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Marcos");
    }

    @Test
    @DisplayName("Should throw exception when id not found")
    void shouldThrowExceptionWhenIdNotFound() {
        when(professionalRepository.findById(111L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> professionalService.findById(111L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Should find professional by email")
    void shouldFindProfessionalByEmail() {
        when(professionalRepository.findByEmail(anyString())).thenReturn(Optional.of(professional));

        ProfessionalResponse response = professionalService.findByEmail("marcos@email.com");

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("marcos@email.com");
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should throw exception when email not found")
    void shouldThrowExceptionWhenEmailNotFound() {
        when(professionalRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> professionalService.findByEmail("marcos@gmail.com"))
                .isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    @DisplayName("Should find all active professionals")
    void shouldFindAllActiveProfessionals() {
        when(professionalRepository.findByActiveTrue()).thenReturn(List.of(professional));

        List<ProfessionalSummaryResponse> response = professionalService.findAllActiveProfessionals();

        assertThat(response).isNotEmpty();
        assertThat(response).hasSize(1);
        assertThat(response).contains(ProfessionalSummaryResponse.from(professional));

    }

    @Test
    @DisplayName("Should inactivate professional successfully")
    void shouldInactivateProfessionalSuccessFully() {
        when(professionalRepository.findById(1L)).thenReturn(Optional.of(professional));
        when(professionalRepository.save(any())).thenReturn(professional);

        professionalService.inactivateProfessional(1L);

        assertThat(professional.getActive()).isFalse();
        verify(professionalRepository).save(any());

    }

    @Test
    @DisplayName("Should throw exception when inactivating non existing professional")
    void shouldThrowExceptionWhenInactivatingNonExistingProfessional() {
        when(professionalRepository.findById(111L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> professionalService.inactivateProfessional(111L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(professionalRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should activate professional successfully")
    void shouldActivateProfessionalSuccessFully() {
        professional.setActive(false);

        when(professionalRepository.findById(1L)).thenReturn(Optional.of(professional));
        when(professionalRepository.save(any())).thenReturn(professional);

        professionalService.activateProfessional(1L);

        assertThat(professional.getActive()).isTrue();
        verify(professionalRepository).save(any());
    }

    @Test
    @DisplayName("Should throw exception when activating non existing professional")
    void shouldThrowExceptionWhenActivatingNonExistingProfessional() {
        when(professionalRepository.findById(111L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> professionalService.activateProfessional(111L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(professionalRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update professional name successfully")
    void shouldUpdateProfessionalNameSuccessfully() {
        updateRequest.setName("Alan");
        when(professionalRepository.findById(1L)).thenReturn(Optional.of(professional));
        when(professionalRepository.save(any())).thenReturn(professional);

        ProfessionalResponse response = professionalService.updateProfessional(1L, updateRequest);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Alan");

        verify(professionalRepository).save(any(Professional.class));

    }

    @Test
    @DisplayName("Should update professional email successfully")
    void shouldUpdateProfessionalEmailSuccessfully() {
        updateRequest.setEmail("alan@email.com");
        when(professionalRepository.findById(1L)).thenReturn(Optional.of(professional));
        when(professionalRepository.existsByEmail("alan@email.com")).thenReturn(false);
        when(professionalRepository.save(any())).thenReturn(professional);

        ProfessionalResponse response = professionalService.updateProfessional(1L, updateRequest);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("alan@email.com");
        verify(professionalRepository).save(any(Professional.class));
    }

    @Test
    @DisplayName("Should update both name and email successfully")
    void shouldUpdateBothNameAndEmailSuccessfully() {
        updateRequest.setName("Alan");
        updateRequest.setEmail("alan@email.com");
        when(professionalRepository.findById(1L)).thenReturn(Optional.of(professional));
        when(professionalRepository.existsByEmail("alan@email.com")).thenReturn(false);
        when(professionalRepository.save(any())).thenReturn(professional);

        ProfessionalResponse response = professionalService.updateProfessional(1L, updateRequest);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Alan");
        assertThat(response.getEmail()).isEqualTo("alan@email.com");
        verify(professionalRepository).save(any(Professional.class));
    }

    @Test
    @DisplayName("Should not update when request is empty")
    void shouldNotUpdateWhenRequestIsEmpty() {
        when(professionalRepository.findById(1L)).thenReturn(Optional.of(professional));
        when(professionalRepository.save(any())).thenReturn(professional);

        ProfessionalResponse response = professionalService.updateProfessional(1L, updateRequest);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Marcos");
        assertThat(response.getEmail()).isEqualTo("marcos@email.com");
        verify(professionalRepository).save(any(Professional.class));
    }

    @Test
    @DisplayName("Should throw exception when updating to existing email")
    void shouldThrowExceptionWhenUpdatingToExistingEmail() {
        updateRequest.setEmail("outro@email.com");

        when(professionalRepository.findById(1L)).thenReturn(Optional.of(professional));
        when(professionalRepository.existsByEmail("outro@email.com")).thenReturn(true);

        assertThatThrownBy(() -> professionalService.updateProfessional(1L, updateRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Professional with email outro@email.com already exists");

        verify(professionalRepository, never()).save(any(Professional.class));
    }

}
