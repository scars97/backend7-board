package com.backend7.frameworkstudy.domain.board.exception;

import lombok.Getter;

@Getter
public class BoardException extends RuntimeException {

    private final BoardResultType errorType;

    public BoardException(BoardResultType errorType) {
        this.errorType = errorType;
    }
}
