package com.backend7.frameworkstudy.domain.member.api;

import com.backend7.frameworkstudy.domain.auth.JwtTokenProvider;
import com.backend7.frameworkstudy.domain.member.dto.LoginRequest;
import com.backend7.frameworkstudy.domain.member.dto.MemberCreateRequest;
import com.backend7.frameworkstudy.domain.member.dto.MemberResponse;
import com.backend7.frameworkstudy.domain.member.exception.MemberException;
import com.backend7.frameworkstudy.domain.member.exception.MemberResultType;
import com.backend7.frameworkstudy.domain.member.service.MemberService;
import com.backend7.frameworkstudy.global.error.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

    private static final String TEST_ACCESS_TOKEN = "TestAccessToken";

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(memberController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @DisplayName("유효하지 않은 입력으로 회원 가입 요청 시 검증 실패 메시지를 반환한다.")
    @Test
    void signUp_InvalidInput() throws Exception {
        // given
        MemberCreateRequest invalidRequest = new MemberCreateRequest("te", "12");

        // when //then
        mockMvc.perform(post("/api/auth/sign-up")
                        .content(objectMapper.writeValueAsString(invalidRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.reason").isNotEmpty())
                .andExpect(jsonPath("$.reason.username").value("최소 4자 이상, 최대 10자 이하여야 합니다."))
                .andExpect(jsonPath("$.reason.password").value("최소 8자 이상, 최대 15자 이하여야 합니다."));
    }

    @DisplayName("회원 가입 시 가입된 회원 정보가 반환된다.")
    @Test
    void signUp() throws Exception {
        // given
        MemberCreateRequest request = new MemberCreateRequest("test1234", "qwer1234");

        given(memberService.signUp(any(MemberCreateRequest.class))).willReturn(new MemberResponse(1L, "test1234"));

        // when //then
        mockMvc.perform(post("/api/auth/sign-up")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.message").value("회원 가입에 성공하였습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.username").value("test1234"));
    }

    @DisplayName("로그인 시 access 토큰이 발급되고 회원 정보가 반환된다.")
    @Test
    void loginUser() throws Exception {
        // given
        LoginRequest request = new LoginRequest("test1234", "12341234");

        given(jwtTokenProvider.generateAccessToken(anyLong())).willReturn(TEST_ACCESS_TOKEN);
        given(memberService.loginUser(any(LoginRequest.class))).willReturn(new MemberResponse(1L, "test1234"));

        // when //then
        mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", "Bearer " + TEST_ACCESS_TOKEN))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("로그인에 성공하였습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.username").value("test1234"));
    }


    @DisplayName("잘못된 회원 정보를 입력한 경우 로그인에 실패한다.")
    @Test
    void loginUser_invalidInput_loginFail() throws Exception {
        // given
        LoginRequest request = new LoginRequest("test1234", "12341234");

        given(memberService.loginUser(any(LoginRequest.class))).willThrow(new MemberException(MemberResultType.MEMBER_NOT_FOUND));

        // when //then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 회원입니다."));
    }

    @DisplayName("잘못된 비밀번호를 입력한 경우 로그인에 실패한다.")
    @Test
    void loginUser_InvalidPassword_loginFail() throws Exception {
        // given
        LoginRequest request = new LoginRequest("test1234", "12341234");

        given(memberService.loginUser(any(LoginRequest.class))).willThrow(new MemberException(MemberResultType.PASSWORD_IS_NOT_MATCH));

        // when //then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("비밀번호가 일치하지 않습니다."));
    }
}