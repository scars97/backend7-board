package com.backend7.frameworkstudy.domain.member.service;

import com.backend7.frameworkstudy.domain.auth.JwtTokenProvider;
import com.backend7.frameworkstudy.domain.member.domain.Member;
import com.backend7.frameworkstudy.domain.member.dto.MemberCreateRequest;
import com.backend7.frameworkstudy.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    private static final String TEST_ACCESS_TOKEN = "TestAccessToken";

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @DisplayName("로그인 성공 시 Access 토큰이 발급된다.")
    @Test
    void loginUser() {
        // given
        Member member = createMember();
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.ofNullable(member));
        given(jwtTokenProvider.generateAccessToken(any(Member.class))).willReturn(TEST_ACCESS_TOKEN);

        // when
        String responseToken = memberService.loginUser(new MemberCreateRequest(member.getUsername(), member.getPassword()));

        // then
        then(responseToken).equals(TEST_ACCESS_TOKEN);

    }

    private Member createMember() {
        return Member.builder()
                .username("member")
                .password("12341234")
                .build();
    }

}