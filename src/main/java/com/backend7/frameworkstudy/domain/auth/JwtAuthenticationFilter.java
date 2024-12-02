package com.backend7.frameworkstudy.domain.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.backend7.frameworkstudy.global.config.SecurityConfig.*;

@Slf4j
@Component
@RequiredArgsConstructor
@Description("JWT 인증이 필요한 url이 요청되는 경우 실행")
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final List<AntPathRequestMatcher> publicGetPaths =
            Arrays.stream(GET_PUBLIC).map(AntPathRequestMatcher::new).toList();
    private final List<AntPathRequestMatcher> publicPostPaths =
            Arrays.stream(POST_PUBLIC).map(AntPathRequestMatcher::new).toList();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            // PUBLIC 경로 검사
            if (isPublicPath(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            log.info("토큰 검증 시작");
            String token = jwtTokenProvider.resolveAccessToken(request);
            if (token == null || !jwtTokenProvider.validateToken(token)) {
                return;
            }
            log.info("토큰 검증 완료");

            // user 정보 불러오기
            MemberDetail memberDetail = jwtTokenProvider.getMember(token);

            // SecurityContextHolder 컨텍스트에 저장
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(memberDetail, null, memberDetail.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);
        } catch (JwtErrorException ex) {
            log.error(ex.getMessage());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(
                    "{\"code\": \"" + HttpStatus.UNAUTHORIZED.value() + "\", "
                            + "\"status\": \"" +  HttpStatus.UNAUTHORIZED.name() + "\", "
                            + "\"message\": \"" + ex.getMessage() + "\"}");
        }
    }

    private boolean isPublicPath(HttpServletRequest request) {
        if (request.getMethod().equals(HttpMethod.GET.toString())) {
            return publicGetPaths.stream().anyMatch(matcher -> matcher.matches(request));
        }
        if (request.getMethod().equals(HttpMethod.POST.toString())) {
            return publicPostPaths.stream().anyMatch(matcher -> matcher.matches(request));
        }
        return false;
    }
}
