package com.backend7.frameworkstudy.domain.member.service;

import com.backend7.frameworkstudy.domain.auth.JwtTokenProvider;
import com.backend7.frameworkstudy.domain.member.domain.Member;
import com.backend7.frameworkstudy.domain.member.dto.MemberCreateRequest;
import com.backend7.frameworkstudy.domain.member.dto.SignUpResponse;
import com.backend7.frameworkstudy.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public SignUpResponse signUp(MemberCreateRequest request) {
        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("중복된 username 입니다.");
        }

        Member member = request.toEntity();
        Member saveMember = memberRepository.save(member);

        return SignUpResponse.of(saveMember);
    }

    public String loginUser(MemberCreateRequest request) {
        Member findMember = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Member Not Found"));

        return jwtTokenProvider.generateAccessToken(findMember);
    }
}
