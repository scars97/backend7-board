package com.backend7.frameworkstudy.domain.board.exception.enumeration;

import com.backend7.frameworkstudy.global.common.ApiResultType;
import org.springframework.http.HttpStatus;

public enum SuccessType implements ApiResultType {

    LOADED_BOARD_LIST(HttpStatus.OK, "게시글 목록 조회 성공"),
    LOADED_BOARD(HttpStatus.OK, "게시글 조회 성공"),
    CREATE_BOARD(HttpStatus.OK, "게시글이 저장되었습니다."),
    EDIT_BOARD(HttpStatus.OK, "게시글이 수정되었습니다."),
    DELETE_BOARD(HttpStatus.NO_CONTENT, "게시글이 삭제되었습니다.");

    private final HttpStatus status;
    private final String message;

    SuccessType(HttpStatus status, String message) {
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
