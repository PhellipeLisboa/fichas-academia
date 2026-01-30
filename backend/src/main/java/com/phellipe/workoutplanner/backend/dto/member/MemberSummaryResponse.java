package com.phellipe.workoutplanner.backend.dto.member;

import com.phellipe.workoutplanner.backend.domain.entity.Member;
import lombok.Data;

@Data
public class MemberSummaryResponse {

    private Long id;
    private String name;
    private String publicCode;

    public static MemberSummaryResponse from(Member member) {
        MemberSummaryResponse response = new MemberSummaryResponse();
        response.setId(member.getId());
        response.setName(member.getName());
        response.setPublicCode(member.getPublicCode());
        return response;
    }

}
