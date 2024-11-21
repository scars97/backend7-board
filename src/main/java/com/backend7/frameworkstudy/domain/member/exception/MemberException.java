package com.backend7.frameworkstudy.domain.member.exception;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {

    private final MemberResultType errorType;

    public MemberException(MemberResultType errorType) {
        this.errorType = errorType;
    }
}
