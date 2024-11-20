package com.backend7.frameworkstudy.domain.member.dto;

import com.backend7.frameworkstudy.domain.member.domain.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCreateRequest {

    @NotBlank
    @Size(min = 4, max = 10, message = "최소 4자 이상, 최대 10자 이하여야 합니다.")
    @Pattern(regexp = "[a-z0-9]+", message = "알파벳 소문자(a~z)와 숫자(0~9)만 가능합니다.")
    private String username;

    @NotBlank
    @Size(min = 8, max = 15, message = "최소 8자 이상, 최대 15자 이하여야 합니다.")
    @Pattern(regexp = "[a-zA-z0-9]+", message = "알파벳 대소문(a~z, A~Z)와 숫자(0~9)만 가능합니다.")
    private String password;

    public Member toEntity() {
        return Member.builder()
                .username(this.username)
                .password(this.password)
                .build();
    }
}
