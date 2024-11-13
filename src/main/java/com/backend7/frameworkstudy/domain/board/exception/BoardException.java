package com.backend7.frameworkstudy.domain.board.exception;

import com.backend7.frameworkstudy.domain.board.exception.enumeration.ErrorType;
import lombok.Getter;

@Getter
public class BoardException extends RuntimeException {

    private final ErrorType errorType;

    public BoardException(ErrorType errorType) {
        this.errorType = errorType;
    }
}
