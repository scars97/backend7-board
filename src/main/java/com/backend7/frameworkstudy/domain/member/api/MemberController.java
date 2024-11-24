package com.backend7.frameworkstudy.domain.member.api;

import com.backend7.frameworkstudy.domain.auth.JwtTokenProvider;
import com.backend7.frameworkstudy.domain.auth.TokenResponse;
import com.backend7.frameworkstudy.domain.member.dto.LoginRequest;
import com.backend7.frameworkstudy.domain.member.dto.MemberCreateRequest;
import com.backend7.frameworkstudy.domain.member.dto.MemberResponse;
import com.backend7.frameworkstudy.domain.member.service.MemberService;
import com.backend7.frameworkstudy.global.common.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.backend7.frameworkstudy.domain.member.exception.MemberResultType.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/sign-up")
    public ApiResponse<MemberResponse> singUp(@Valid @RequestBody MemberCreateRequest request) {
        return ApiResponse.ok(SIGN_UP, memberService.signUp(request));
    }

    @PostMapping("/login")
    public ApiResponse<MemberResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        MemberResponse memberResponse = memberService.loginUser(request);

        // TODO 토큰 발급을 controller에서 하는 것은 조금 어색하다.
        Cookie cookie = new Cookie("refresh", jwtTokenProvider.generateRefreshToken(memberResponse.getId()));
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/**");

        response.addCookie(cookie);
        response.addHeader("Authorization", "Bearer " + jwtTokenProvider.generateAccessToken(memberResponse.getId()));
        return ApiResponse.ok(LOGIN, memberService.loginUser(request));
    }

    // refresh 토큰을 사용한 access 토큰 재발급
    @GetMapping("/renew-token")
    public ApiResponse<TokenResponse> renewToken(@CookieValue(name = "refresh") String refreshToken) {
        return ApiResponse.ok(REISSUE_TOKEN, memberService.renewToken(refreshToken));
    }

}
