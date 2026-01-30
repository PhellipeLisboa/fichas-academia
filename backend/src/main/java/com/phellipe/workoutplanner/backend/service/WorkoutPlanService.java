package com.phellipe.workoutplanner.backend.service;

import com.phellipe.workoutplanner.backend.domain.entity.Member;
import com.phellipe.workoutplanner.backend.domain.entity.Professional;
import com.phellipe.workoutplanner.backend.domain.entity.WorkoutPlan;
import com.phellipe.workoutplanner.backend.domain.enumtype.WorkoutPlanStatus;
import com.phellipe.workoutplanner.backend.domain.repository.WorkoutPlanRepository;
import com.phellipe.workoutplanner.backend.dto.workoutplan.CreateWorkoutPlanRequest;
import com.phellipe.workoutplanner.backend.dto.workoutplan.UpdateWorkoutPlanRequest;
import com.phellipe.workoutplanner.backend.dto.workoutplan.WorkoutPlanResponse;
import com.phellipe.workoutplanner.backend.dto.workoutplan.WorkoutPlanSummaryResponse;
import com.phellipe.workoutplanner.backend.exception.BusinessException;
import com.phellipe.workoutplanner.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final MemberService memberService;
    private final ProfessionalService professionalService;

    @Transactional
    public WorkoutPlanResponse createWorkoutPlan(CreateWorkoutPlanRequest request) {
        Member member = memberService.getMemberEntity(request.getMemberId());
        Professional professional = professionalService.getProfessionalEntity(request.getProfessionalId());

        if (!member.getActive()) {
            throw new BusinessException("Cannot create workout plan for inactive member");
        }

        if (!professional.getActive()) {
            throw new BusinessException("Cannot create workout plan with inactive professional");
        }

        WorkoutPlan workoutPlan = new WorkoutPlan();
        workoutPlan.setMember(member);
        workoutPlan.setProfessional(professional);
        workoutPlan.setStartDate(request.getStartDate());
        workoutPlan.setReviewDate(request.getReviewDate());
        workoutPlan.setReassessmentDate(request.getReassessmentDate());
        workoutPlan.setSheetNumber(request.getSheetNumber());
        workoutPlan.setRestSeconds(request.getRestSeconds());
        workoutPlan.setIntensity(request.getIntensity());
        workoutPlan.setNotes(request.getNotes());
        workoutPlan.setStatus(WorkoutPlanStatus.DRAFT);

        WorkoutPlan savedPlan = workoutPlanRepository.save(workoutPlan);
        return WorkoutPlanResponse.from(savedPlan);

    }

    @Transactional
    public WorkoutPlanResponse updateWorkoutPlan(Long id, UpdateWorkoutPlanRequest request) {
        WorkoutPlan workoutPlan = getWorkoutPlanEntity(id);

        if (workoutPlan.getStatus() == WorkoutPlanStatus.INACTIVE) {
            throw new BusinessException("Cannot update inactive workout plan");
        }

        if (request.getReviewDate() != null) {
            workoutPlan.setReviewDate(request.getReviewDate());
        }
        if (request.getReassessmentDate() != null) {
            workoutPlan.setReassessmentDate(request.getReassessmentDate());
        }
        if (request.getSheetNumber() != null) {
            workoutPlan.setSheetNumber(request.getSheetNumber());
        }
        if (request.getRestSeconds() != null) {
            workoutPlan.setRestSeconds(request.getRestSeconds());
        }
        if (request.getIntensity() != null) {
            workoutPlan.setIntensity(request.getIntensity());
        }
        if (request.getNotes() != null) {
            workoutPlan.setNotes(request.getNotes());
        }

        WorkoutPlan updatedPlan = workoutPlanRepository.save(workoutPlan);
        return WorkoutPlanResponse.from(updatedPlan);

    }

    @Transactional
    public WorkoutPlanResponse finalizeWorkoutPlan(Long id) {
        WorkoutPlan workoutPlan = getWorkoutPlanEntity(id);

        if (workoutPlan.getStatus() != WorkoutPlanStatus.DRAFT) {
            throw new BusinessException("Workout plan is already finalized");
        }

        validateWorkoutPlanStructure(workoutPlan);

        inactivatePreviousPlans(workoutPlan.getMember().getId());

        workoutPlan.setStatus(WorkoutPlanStatus.ACTIVE);

        WorkoutPlan finalizedPlan = workoutPlanRepository.save(workoutPlan);
        return WorkoutPlanResponse.from(finalizedPlan);

    }

    @Transactional
    public WorkoutPlanResponse createReassessmentPlan(CreateWorkoutPlanRequest request) {
        inactivatePreviousPlans(request.getMemberId());

        return createWorkoutPlan(request);
    }

    public WorkoutPlanResponse findActivePlanByMemberId(Long memberId) {
        WorkoutPlan activePlan = workoutPlanRepository.findByMemberIdAndStatus(memberId, WorkoutPlanStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("No active workout plan for member"));
        return WorkoutPlanResponse.from(activePlan);
    }

    public WorkoutPlanResponse findActivePlanByPublicCode(String publicCode) {
        WorkoutPlan activePlan = workoutPlanRepository.findByMemberPublicCodeAndStatus(publicCode, WorkoutPlanStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("No active workout plan found for this code"));
        return WorkoutPlanResponse.from(activePlan);
    }

    public List<WorkoutPlanSummaryResponse> findAllByMemberId(Long memberId) {
        return workoutPlanRepository.findByMemberId(memberId)
                .stream()
                .map(WorkoutPlanSummaryResponse::from)
                .toList();
    }

    public List<WorkoutPlanSummaryResponse> findAllByProfessionalId(Long professionalId) {
        return workoutPlanRepository.findByProfessionalId(professionalId)
                .stream()
                .map(WorkoutPlanSummaryResponse::from)
                .toList();
    }

    public WorkoutPlan getWorkoutPlanEntity(Long id) {
        return workoutPlanRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Workout plan not found with id: " + id)
        );
    }

    private void validateWorkoutPlanStructure(WorkoutPlan workoutPlan) {
        if (workoutPlan.getWorkouts() == null || workoutPlan.getWorkouts().isEmpty()) {
            throw new BusinessException("Workout plan must have at least one workout");
        }

        for (var workout : workoutPlan.getWorkouts()) {
            if (workout.getBlocks() == null || workout.getBlocks().isEmpty()) {
                throw new BusinessException("Workout '" + workout.getName() + "' must have at least one block");
            }

            for (var block : workout.getBlocks()) {
                if (block.getItems() == null || block.getItems().isEmpty()) {
                    throw new BusinessException("Block in workout '" + workout.getName() + "' cannot be empty");
                }
            }

        }

    }

    private void inactivatePreviousPlans(Long memberId) {
        List<WorkoutPlan> activePlans = workoutPlanRepository.findAllByMemberIdAndStatus(memberId, WorkoutPlanStatus.ACTIVE);

        for (WorkoutPlan plan : activePlans) {
            plan.setStatus(WorkoutPlanStatus.INACTIVE);
            workoutPlanRepository.save(plan);
        }

    }

}
