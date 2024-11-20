package com.backend7.frameworkstudy.domain.member.dto;

import com.backend7.frameworkstudy.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpResponse {

    private Long id;
    private String username;

    @Builder
    private SignUpResponse(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public static SignUpResponse of(Member member) {
        return SignUpResponse.builder()
                .id(member.getId())
                .username(member.getUsername())
                .build();
    }
}
