package com.phellipe.workoutplanner.backend.dto.member;

import com.phellipe.workoutplanner.backend.domain.entity.Member;
import lombok.Data;

@Data
public class MemberResponse {

    private Long id;
    private String name;
    private String publicCode;
    private Boolean active;

    public static MemberResponse from(Member member) {
        MemberResponse response = new MemberResponse();
        response.setId(member.getId());
        response.setName(member.getName());
        response.setPublicCode(member.getPublicCode());
        response.setActive(member.getActive());
        return response;
    }

}
