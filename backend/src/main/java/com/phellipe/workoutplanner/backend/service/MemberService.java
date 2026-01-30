package com.phellipe.workoutplanner.backend.service;

import com.phellipe.workoutplanner.backend.domain.entity.Member;
import com.phellipe.workoutplanner.backend.domain.repository.MemberRepository;
import com.phellipe.workoutplanner.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member createMember(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Member name cannot be empty");
        }

        Member member = new Member();
        member.setName(name.trim());
        member.setPublicCode(generateUniquePublicCode());
        member.setActive(true);

        return memberRepository.save(member);

    }

    public Member findByPublicCode(String publicCode) {
        return memberRepository.findByPublicCode(publicCode).orElseThrow(
                () -> new ResourceNotFoundException("Member not found with public code: " + publicCode));
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Member not found with id: " + id));
    }

    public List<Member> findAllActiveMembers() {
        return memberRepository.findByActiveTrue();
    }

    @Transactional
    public void inactivateMember(Long memberId) {
        Member member = findById(memberId);
        member.setActive(false);
        memberRepository.save(member);
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
