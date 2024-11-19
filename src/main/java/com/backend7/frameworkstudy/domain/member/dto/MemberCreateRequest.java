package com.backend7.frameworkstudy.domain.member.dto;

import com.backend7.frameworkstudy.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberCreateRequest {

    private String username;
    private String password;

    public Member toEntity() {
        return Member.builder()
                .username(this.username)
                .password(this.password)
                .build();
    }
}
