package com.backend7.frameworkstudy.domain.member.api;

import com.backend7.frameworkstudy.domain.auth.JwtTokenProvider;
import com.backend7.frameworkstudy.domain.member.dto.LoginRequest;
import com.backend7.frameworkstudy.domain.member.dto.MemberCreateRequest;
import com.backend7.frameworkstudy.domain.member.dto.MemberResponse;
import com.backend7.frameworkstudy.domain.member.service.MemberService;
import com.backend7.frameworkstudy.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.backend7.frameworkstudy.domain.member.exception.MemberResultType.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "회원 API", description = "회원가입 및 로그인 기능")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "회원가입")
    public ApiResponse<MemberResponse> singUp(@Valid @RequestBody MemberCreateRequest request) {
        return ApiResponse.ok(SIGN_UP, memberService.signUp(request));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ApiResponse<MemberResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        MemberResponse memberResponse = memberService.loginUser(request);

        // TODO 토큰 발급을 controller에서 하는 것은 조금 어색하다.
        response.addHeader("Authorization", "Bearer " + jwtTokenProvider.generateAccessToken(memberResponse.getId()));
        return ApiResponse.ok(LOGIN, memberService.loginUser(request));
    }

}
