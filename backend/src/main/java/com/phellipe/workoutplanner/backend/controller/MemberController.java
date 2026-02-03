package com.phellipe.workoutplanner.backend.controller;

import com.phellipe.workoutplanner.backend.dto.member.CreateMemberRequest;
import com.phellipe.workoutplanner.backend.dto.member.MemberResponse;
import com.phellipe.workoutplanner.backend.dto.member.MemberSummaryResponse;
import com.phellipe.workoutplanner.backend.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody CreateMemberRequest request) {
        MemberResponse response = memberService.createMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable Long id) {
        MemberResponse response = memberService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public-code/{publicCode}")
    public ResponseEntity<MemberResponse> getMemberByPublicCode(@PathVariable String publicCode) {
        MemberResponse response = memberService.findByPublicCode(publicCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<MemberSummaryResponse>> getActiveMembers() {
        List<MemberSummaryResponse> response = memberService.findAllActiveMembers();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/inactivate")
    public ResponseEntity<Void> inactivateMember(@PathVariable Long id) {
        memberService.inactivateMember(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/inactivate")
    public ResponseEntity<Void> activateMember(@PathVariable Long id) {
        memberService.activateMember(id);
        return ResponseEntity.noContent().build();
    }

}
