package com.backend7.frameworkstudy.domain.board.exception;

import lombok.Getter;

@Getter
public class BoardException extends RuntimeException {

    private final ErrorType errorType;

    public BoardException(ErrorType errorType) {
        this.errorType = errorType;
    }
}
