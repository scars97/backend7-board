package com.backend7.frameworkstudy.domain.board.exception.enumeration;

import com.backend7.frameworkstudy.global.common.ApiResultType;
import org.springframework.http.HttpStatus;

public enum BoardResultType implements ApiResultType {

    // Success
    LOADED_BOARD_LIST(HttpStatus.OK, "게시글 목록 조회 성공"),
    LOADED_BOARD(HttpStatus.OK, "게시글 조회 성공"),
    CREATE_BOARD(HttpStatus.OK, "게시글이 저장되었습니다."),
    EDIT_BOARD(HttpStatus.OK, "게시글이 수정되었습니다."),
    DELETE_BOARD(HttpStatus.NO_CONTENT, "게시글이 삭제되었습니다."),

    // Error
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않은 게시글입니다."),
    PASSWORD_IS_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");

    private final HttpStatus status;
    private final String message;

    BoardResultType(HttpStatus status, String message) {
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
