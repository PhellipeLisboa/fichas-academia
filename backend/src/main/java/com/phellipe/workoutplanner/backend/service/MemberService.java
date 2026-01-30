package com.phellipe.workoutplanner.backend.service;

import com.phellipe.workoutplanner.backend.domain.entity.Member;
import com.phellipe.workoutplanner.backend.domain.repository.MemberRepository;
import com.phellipe.workoutplanner.backend.dto.member.CreateMemberRequest;
import com.phellipe.workoutplanner.backend.dto.member.MemberResponse;
import com.phellipe.workoutplanner.backend.dto.member.MemberSummaryResponse;
import com.phellipe.workoutplanner.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse createMember(CreateMemberRequest request) {

        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Member name cannot be empty");
        }

        Member member = new Member();
        member.setName(request.getName().trim());
        member.setPublicCode(generateUniquePublicCode());
        member.setActive(true);

        return MemberResponse.from(memberRepository.save(member));

    }

    public MemberResponse findByPublicCode(String publicCode) {
        Member member = memberRepository.findByPublicCode(publicCode).orElseThrow(
                () -> new ResourceNotFoundException("Member not found with public code: " + publicCode));

        return MemberResponse.from(member);
    }

    public MemberResponse findById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Member not found with id: " + id));

        return MemberResponse.from(member);
    }

    public List<MemberSummaryResponse> findAllActiveMembers() {
        return memberRepository.findByActiveTrue()
                .stream()
                .map(MemberSummaryResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void inactivateMember(Long Id) {
        Member member = getMemberEntity(Id);
        member.setActive(false);
        memberRepository.save(member);
    }

    @Transactional
    public void activateMember(Long Id) {
        Member member = getMemberEntity(Id);
        member.setActive(true);
        memberRepository.save(member);
    }

    private Member getMemberEntity(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Member not found with id: " + id));
    }

    private String generateUniquePublicCode() {
        String code;
        do {
            code = generateRandomCode();
        } while (memberRepository.existsByPublicCode(code));

        return code;
    }

    private String generateRandomCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }

        return code.toString();
    }

}
