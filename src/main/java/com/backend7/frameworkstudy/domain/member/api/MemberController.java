package com.backend7.frameworkstudy.domain.member.api;

import com.backend7.frameworkstudy.domain.auth.JwtTokenProvider;
import com.backend7.frameworkstudy.domain.member.dto.LoginRequest;
import com.backend7.frameworkstudy.domain.member.dto.MemberCreateRequest;
import com.backend7.frameworkstudy.domain.member.dto.MemberResponse;
import com.backend7.frameworkstudy.domain.member.exception.MemberResultType;
import com.backend7.frameworkstudy.domain.member.service.MemberService;
import com.backend7.frameworkstudy.global.common.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/sign-up")
    public ApiResponse<MemberResponse> singUp(@Valid @RequestBody MemberCreateRequest request) {
        return ApiResponse.ok(MemberResultType.SIGN_UP, memberService.signUp(request));
    }

    @PostMapping("/login")
    public ApiResponse<MemberResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        MemberResponse memberResponse = memberService.loginUser(request);

        // access 토큰 발급을 controller에서 하는 것은 조금 어색하다. - 고민필요
        response.addHeader("Authorization", "Bearer " + jwtTokenProvider.generateAccessToken(memberResponse.getId()));
        return ApiResponse.ok(MemberResultType.LOGIN, memberService.loginUser(request));
    }

    // refresh 토큰을 사용한 access 토큰 재발급
}
