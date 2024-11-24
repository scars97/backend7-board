package com.backend7.frameworkstudy.domain.member.api;

import com.backend7.frameworkstudy.domain.auth.JwtTokenProvider;
import com.backend7.frameworkstudy.domain.auth.TokenResponse;
import com.backend7.frameworkstudy.domain.member.dto.LoginRequest;
import com.backend7.frameworkstudy.domain.member.dto.MemberCreateRequest;
import com.backend7.frameworkstudy.domain.member.dto.MemberResponse;
import com.backend7.frameworkstudy.domain.member.service.MemberService;
import com.backend7.frameworkstudy.global.error.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
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
    private static final String TEST_REFRESH_TOKEN = "TestRefreshToken";

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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("회원 가입에 성공하였습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.username").value("test1234"));
    }

    @DisplayName("로그인 시 access, refresh 토큰이 발급되고 회원 정보가 반환된다.")
    @Test
    void loginUser() throws Exception {
        // given
        LoginRequest request = new LoginRequest("test1234", "12341234");

        given(jwtTokenProvider.generateAccessToken(anyLong())).willReturn(TEST_ACCESS_TOKEN);
        given(jwtTokenProvider.generateRefreshToken(anyLong())).willReturn(TEST_REFRESH_TOKEN);
        given(memberService.loginUser(any(LoginRequest.class))).willReturn(new MemberResponse(1L, "test1234"));

        // when //then
        mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(cookie().value("refresh", TEST_REFRESH_TOKEN))
                .andExpect(cookie().httpOnly("refresh", true))
                .andExpect(cookie().path("refresh", "/api/**"))
                .andExpect(header().string("Authorization", "Bearer " + TEST_ACCESS_TOKEN))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("로그인에 성공하였습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.username").value("test1234"));
    }

    @DisplayName("refresh 토큰으로 재발급 요청 시 새로운 access 토큰이 발급된다.")
    @Test
    void renewToken() throws Exception {
        // given
        String refreshToken = "valid-refresh-token";
        String newAccessToken = "new-access-token";
        Cookie cookie = new Cookie("refresh", refreshToken);

        given(memberService.renewToken(refreshToken)).willReturn(TokenResponse.of(newAccessToken, refreshToken));

        // when //then
        mockMvc.perform(get("/api/auth/renew-token")
                    .cookie(cookie)
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("토큰이 재발급되었습니다."))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.accessToken").value(newAccessToken))
                .andExpect(jsonPath("$.data.refreshToken").value(refreshToken));
    }
}