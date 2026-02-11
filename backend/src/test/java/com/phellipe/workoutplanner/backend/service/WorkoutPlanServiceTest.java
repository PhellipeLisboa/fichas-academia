package com.phellipe.workoutplanner.backend.service;

import com.phellipe.workoutplanner.backend.domain.entity.Member;
import com.phellipe.workoutplanner.backend.domain.entity.Professional;
import com.phellipe.workoutplanner.backend.domain.entity.WorkoutPlan;
import com.phellipe.workoutplanner.backend.domain.enumtype.Intensity;
import com.phellipe.workoutplanner.backend.domain.enumtype.WorkoutPlanStatus;
import com.phellipe.workoutplanner.backend.domain.repository.MemberRepository;
import com.phellipe.workoutplanner.backend.domain.repository.ProfessionalRepository;
import com.phellipe.workoutplanner.backend.domain.repository.WorkoutPlanRepository;
import com.phellipe.workoutplanner.backend.dto.workoutPlan.CreateWorkoutPlanRequest;
import com.phellipe.workoutplanner.backend.dto.workoutPlan.UpdateWorkoutPlanRequest;
import com.phellipe.workoutplanner.backend.dto.workoutPlan.WorkoutPlanResponse;
import com.phellipe.workoutplanner.backend.exception.BusinessException;
import com.phellipe.workoutplanner.backend.exception.ResourceNotFoundException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WorkoutPlanService Tests")
public class WorkoutPlanServiceTest {

    @Mock
    private WorkoutPlanRepository workoutPlanRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private ProfessionalService professionalService;

    @InjectMocks
    private WorkoutPlanService workoutPlanService;


    private WorkoutPlan workoutPlan;
    private Member member;
    private Professional professional;
    private CreateWorkoutPlanRequest createRequest;
    private UpdateWorkoutPlanRequest updateRequest;

    @BeforeEach
    void setUp() {

        member = new Member();
        member.setId(1L);
        member.setName("Paulo");
        member.setPublicCode("ABC12345");
        member.setActive(true);

        professional = new Professional();

        professional.setId(1L);
        professional.setName("Marcos");
        professional.setEmail("marcos@email.com");
        professional.setActive(true);

        workoutPlan = new WorkoutPlan();
        workoutPlan.setId(1L);
        workoutPlan.setMember(member);
        workoutPlan.setProfessional(professional);
        workoutPlan.setStartDate(LocalDate.of(2026, 2, 10));
        workoutPlan.setReviewDate(LocalDate.of(2026, 3, 10));
        workoutPlan.setReassessmentDate(LocalDate.of(2026, 5, 10));
        workoutPlan.setSheetNumber(1);
        workoutPlan.setRestSeconds(90);
        workoutPlan.setIntensity(Intensity.HIGH);
        workoutPlan.setStatus(WorkoutPlanStatus.DRAFT);
        workoutPlan.setNotes("notes");
        workoutPlan.setWorkouts(new ArrayList<>());

        createRequest = new CreateWorkoutPlanRequest();
        createRequest.setMemberId(1L);
        createRequest.setProfessionalId(1L);
        createRequest.setStartDate(LocalDate.of(2026, 2, 10));
        createRequest.setReviewDate(LocalDate.of(2026, 3, 10));
        createRequest.setReassessmentDate(LocalDate.of(2026, 5, 10));
        createRequest.setSheetNumber(1);
        createRequest.setRestSeconds(90);
        createRequest.setIntensity(Intensity.HIGH);
        createRequest.setNotes("notes");

        updateRequest = new UpdateWorkoutPlanRequest();

    }

    @Test
    @DisplayName("Should create workout plan successfully")
    void shouldCreateWorkoutPlanSuccessfully() {
        when(memberService.getMemberEntity(1L)).thenReturn(member);
        when(professionalService.getProfessionalEntity(1L)).thenReturn(professional);
        when(workoutPlanRepository.save(any())).thenReturn(workoutPlan);

        WorkoutPlanResponse response = workoutPlanService.createWorkoutPlan(createRequest);

        assertThat(response).isNotNull();
        assertThat(response.getMemberId()).isEqualTo(1L);
        assertThat(response.getProfessionalId()).isEqualTo(1L);
        assertThat(response.getStatus()).isEqualTo(WorkoutPlanStatus.DRAFT);
        assertThat(response.getIntensity()).isEqualTo(Intensity.HIGH);
        assertThat(response.getRestSeconds()).isEqualTo(90);
        assertThat(response.getNotes()).isEqualTo("notes");

        verify(workoutPlanRepository, times(1)).save(any(WorkoutPlan.class));

    }

    @Test
    @DisplayName("Should throw exception when creating plan for inactive member")
    void shouldThrowExceptionWhenCreatingPlanForInactiveMember() {
        member.setActive(false);
        when(memberService.getMemberEntity(1L)).thenReturn(member);
        when(professionalService.getProfessionalEntity(1L)).thenReturn(professional);

        assertThatThrownBy(() -> workoutPlanService.createWorkoutPlan(createRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Cannot create workout plan for inactive member");

        verify(workoutPlanRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when creating plan with inactive professional")
    void shouldThrowExceptionWhenCreatingPlanWithInactiveProfessional() {
        professional.setActive(false);
        when(memberService.getMemberEntity(1L)).thenReturn(member);
        when(professionalService.getProfessionalEntity(1L)).thenReturn(professional);

        assertThatThrownBy(() -> workoutPlanService.createWorkoutPlan(createRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Cannot create workout plan with inactive professional");

        verify(workoutPlanRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update workout plan in draft status")
    void shouldUpdateWorkoutPlanInDraftStatus() {

        updateRequest.setRestSeconds(60);
        updateRequest.setIntensity(Intensity.MEDIUM);
        updateRequest.setNotes("notes notes");

        when(workoutPlanRepository.findById(1L)).thenReturn(Optional.of(workoutPlan));
        when(workoutPlanRepository.save(any())).thenReturn(workoutPlan);

        WorkoutPlanResponse response = workoutPlanService.updateWorkoutPlan(1L, updateRequest);

        assertThat(response).isNotNull();
        assertThat(response.getRestSeconds()).isEqualTo(60);
        assertThat(response.getIntensity()).isEqualTo(Intensity.MEDIUM);
        assertThat(response.getNotes()).isEqualTo("notes notes");

        verify(workoutPlanRepository).save(any());

    }

    @Test
    @DisplayName("Should update workout plan in active status")
    void shouldUpdateWorkoutPlanInActiveStatus() {

        workoutPlan.setStatus(WorkoutPlanStatus.ACTIVE);
        updateRequest.setRestSeconds(60);

        when(workoutPlanRepository.findById(1L)).thenReturn(Optional.of(workoutPlan));
        when(workoutPlanRepository.save(any())).thenReturn(workoutPlan);

        WorkoutPlanResponse response = workoutPlanService.updateWorkoutPlan(1L, updateRequest);

        assertThat(response).isNotNull();
        assertThat(response.getRestSeconds()).isEqualTo(60);

        verify(workoutPlanRepository).save(any());

    }

    @Test
    @DisplayName("Should throw exception when updating inactive workout plan")
    void shouldThrowExceptionWhenUpdatingInactiveWorkoutPlan() {

        workoutPlan.setStatus(WorkoutPlanStatus.INACTIVE);
        updateRequest.setRestSeconds(60);

        when(workoutPlanRepository.findById(1L)).thenReturn(Optional.of(workoutPlan));

        assertThatThrownBy(() -> workoutPlanService.updateWorkoutPlan(1L, updateRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Cannot update inactive workout plan");

        verify(workoutPlanRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when finalizing empty workout plan")
    void shouldThrowExceptionWhenFinalizingEmptyWorkoutPlan() {
        when(workoutPlanRepository.findById(1L)).thenReturn(Optional.of(workoutPlan));

        assertThatThrownBy(() -> workoutPlanService.finalizeWorkoutPlan(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Workout plan must have at least one workout");

        verify(workoutPlanRepository, never()).save(any());

    }

    @Test
    @DisplayName("Should throw exception when finalizing already finalized plan")
    void shouldThrowExceptionWhenFinalizingAlreadyFinalizedPlan() {
        workoutPlan.setStatus(WorkoutPlanStatus.ACTIVE);
        when(workoutPlanRepository.findById(1L)).thenReturn(Optional.of(workoutPlan));

        assertThatThrownBy(() -> workoutPlanService.finalizeWorkoutPlan(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Workout plan is already finalized");

        verify(workoutPlanRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find active plan by member id")
    void shouldFindActivePlanByMemberId() {
        workoutPlan.setStatus(WorkoutPlanStatus.ACTIVE);

        when(workoutPlanRepository.findByMemberIdAndStatus(1L, WorkoutPlanStatus.ACTIVE)).thenReturn(Optional.of(workoutPlan));

        WorkoutPlanResponse response = workoutPlanService.findActivePlanByMemberId(1L);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(WorkoutPlanStatus.ACTIVE);
        assertThat(response.getMemberId()).isEqualTo(1L);

    }

    @Test
    @DisplayName("Should throw exception when no active plan found for member")
    void shouldThrowExceptionWhenNoActivePlanFoundForMember() {

        when(workoutPlanRepository.findByMemberIdAndStatus(1L, WorkoutPlanStatus.ACTIVE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workoutPlanService.findActivePlanByMemberId(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("No active workout plan for member");

    }

    @Test
    @DisplayName("Should find active plan by public code")
    void shouldFindActivePlanByPublicCode() {
        workoutPlan.setStatus(WorkoutPlanStatus.ACTIVE);

        when(workoutPlanRepository.findByMemberPublicCodeAndStatus("ABC12345", WorkoutPlanStatus.ACTIVE)).thenReturn(Optional.of(workoutPlan));

        WorkoutPlanResponse response = workoutPlanService.findActivePlanByPublicCode("ABC12345");

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(WorkoutPlanStatus.ACTIVE);
        assertThat(response.getMemberId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should throw exception when no active plan found for public code")
    void shouldThrowExceptionWhenNoActivePlanFoundForPublicCode() {
        when(workoutPlanRepository.findByMemberPublicCodeAndStatus("INVALID", WorkoutPlanStatus.ACTIVE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> workoutPlanService.findActivePlanByPublicCode("INVALID"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("No active workout plan found for this code");
    }

}
