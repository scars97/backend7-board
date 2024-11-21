package com.backend7.frameworkstudy.domain.member.dto;

import com.backend7.frameworkstudy.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {

    private Long id;
    private String username;

    @Builder
    private MemberResponse(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .username(member.getUsername())
                .build();
    }
}
