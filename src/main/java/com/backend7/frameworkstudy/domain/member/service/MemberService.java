package com.backend7.frameworkstudy.domain.member.service;

import com.backend7.frameworkstudy.domain.auth.JwtTokenProvider;
import com.backend7.frameworkstudy.domain.auth.TokenResponse;
import com.backend7.frameworkstudy.domain.member.domain.Member;
import com.backend7.frameworkstudy.domain.member.dto.LoginRequest;
import com.backend7.frameworkstudy.domain.member.dto.MemberCreateRequest;
import com.backend7.frameworkstudy.domain.member.dto.MemberResponse;
import com.backend7.frameworkstudy.domain.member.exception.MemberException;
import com.backend7.frameworkstudy.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.backend7.frameworkstudy.domain.member.exception.MemberResultType.*;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public MemberResponse signUp(MemberCreateRequest request) {
        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new MemberException(DUPLICATE_USERNAME);
        }

        Member member = request.toEntity();
        Member saveMember = memberRepository.save(member);

        return MemberResponse.of(saveMember);
    }

    public MemberResponse loginUser(LoginRequest request) {
        Member findMember = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        if (!request.getPassword().equals(findMember.getPassword())) {
            throw new MemberException(PASSWORD_IS_NOT_MATCH);
        }

        return MemberResponse.of(findMember);
    }

    public TokenResponse renewToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new MemberException(RETRY_LOGIN);
        }

        Long id = jwtTokenProvider.getId(refreshToken);
        Member findMember = memberRepository.findById(id).orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        return TokenResponse.of(
                "Bearer " + jwtTokenProvider.generateAccessToken(findMember.getId()),
                jwtTokenProvider.generateRefreshToken(findMember.getId())
        );
    }
}
