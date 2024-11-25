package com.backend7.frameworkstudy.domain.member.exception;

import com.backend7.frameworkstudy.global.common.ApiResultType;
import org.springframework.http.HttpStatus;

public enum MemberResultType implements ApiResultType {

    // Success
    SIGN_UP(HttpStatus.OK, "회원 가입에 성공하였습니다."),
    LOGIN(HttpStatus.OK, "로그인에 성공하였습니다."),

    // Error
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "중복된 username 입니다."),
    PASSWORD_IS_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    RETRY_LOGIN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다. 다시 로그인해주세요.");

    private final HttpStatus status;
    private final String message;

    MemberResultType(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return this.status;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
