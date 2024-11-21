package com.backend7.frameworkstudy.domain.member.service;

import com.backend7.frameworkstudy.domain.auth.JwtTokenProvider;
import com.backend7.frameworkstudy.domain.member.domain.Member;
import com.backend7.frameworkstudy.domain.member.dto.LoginRequest;
import com.backend7.frameworkstudy.domain.member.dto.MemberCreateRequest;
import com.backend7.frameworkstudy.domain.member.dto.MemberResponse;
import com.backend7.frameworkstudy.domain.member.exception.MemberException;
import com.backend7.frameworkstudy.domain.member.exception.MemberResultType;
import com.backend7.frameworkstudy.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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

    @DisplayName("회원 가입 시 중복된 username이 존재하면 예외가 발생한다.")
    @Test
    void signUp_duplicateUsername_throwException() {
        // given
        given(memberRepository.existsByUsername(anyString())).willReturn(true);

        // when //then
        assertThatThrownBy(() -> memberService.signUp(new MemberCreateRequest("member", "12341234")))
                .isInstanceOf(MemberException.class)
                .hasFieldOrPropertyWithValue("errorType", MemberResultType.DUPLICATE_USERNAME)
                .extracting("errorType")
                .extracting("status", "message")
                .containsExactly(HttpStatus.BAD_REQUEST, "중복된 username 입니다.");
        verify(memberRepository, times(1)).existsByUsername(anyString());
    }

    @DisplayName("회원 가입에 성공 한다.")
    @Test
    void signUp_success() {
        // given
        Member mockMember = mock(Member.class);
        given(memberRepository.save(any(Member.class))).willReturn(mockMember);

        // when
        MemberResponse response = memberService.signUp(new MemberCreateRequest("member", "12341234"));

        //then
        assertThat(response).isNotNull();
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @DisplayName("로그인 시 존재하지 않는 회원인 경우 예외가 발생한다.")
    @Test
    void loginUser_notFound_ThrowException() {
        // given
        Member member = createMember();
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.empty());

        // when //then
        assertThatThrownBy(() -> memberService.loginUser(new LoginRequest("test1234", "12341234")))
                .isInstanceOf(MemberException.class)
                .hasFieldOrPropertyWithValue("errorType", MemberResultType.MEMBER_NOT_FOUND)
                .extracting("errorType")
                .extracting("status", "message")
                .containsExactly(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다.");
        verify(memberRepository, times(1)).findByUsername(anyString());
    }

    @DisplayName("로그인 시 비밀번호가 일치하지 않으면 예외가 발생한다.")
    @Test
    void loginUser_passwordNotMatched_ThrowException() {
        // given
        Member member = createMember();
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.ofNullable(member));

        // when //then
        assertThatThrownBy(() -> memberService.loginUser(new LoginRequest("test1234", "1111")))
                .isInstanceOf(MemberException.class)
                .hasFieldOrPropertyWithValue("errorType", MemberResultType.PASSWORD_IS_NOT_MATCH)
                .extracting("errorType")
                .extracting("status", "message")
                .containsExactly(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        verify(memberRepository, times(1)).findByUsername(anyString());
    }

    @DisplayName("로그인 성공 시 회원 정보가 반환된다.")
    @Test
    void loginUser() {
        // given
        Member member = createMember();
        given(memberRepository.findByUsername(anyString())).willReturn(Optional.ofNullable(member));

        // when
        MemberResponse memberResponse = memberService.loginUser(new LoginRequest("member", "12341234"));

        // then
        assertThat(memberResponse).isNotNull();
        verify(memberRepository, times(1)).findByUsername(anyString());
    }

    private Member createMember() {
        return Member.builder()
                .username("member")
                .password("12341234")
                .build();
    }

}