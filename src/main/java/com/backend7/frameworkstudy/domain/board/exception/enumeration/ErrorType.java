package com.backend7.frameworkstudy.domain.board.exception.enumeration;

import com.backend7.frameworkstudy.global.common.ApiResultType;
import org.springframework.http.HttpStatus;

public enum ErrorType implements ApiResultType {

    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않은 게시글입니다."),
    PASSWORD_IS_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorType(HttpStatus status, String message) {
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
