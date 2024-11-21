package com.backend7.frameworkstudy.domain.member.service;

import com.backend7.frameworkstudy.domain.auth.JwtTokenProvider;
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

@Transactional
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

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

        findMember.renewToken(jwtTokenProvider.generateRefreshToken(findMember.getId()));
        return MemberResponse.of(findMember);
    }
}
