package com.backend7.frameworkstudy.domain.board.exception;

import com.backend7.frameworkstudy.domain.board.exception.enumeration.BoardResultType;
import lombok.Getter;

@Getter
public class BoardException extends RuntimeException {

    private final BoardResultType errorType;

    public BoardException(BoardResultType errorType) {
        this.errorType = errorType;
    }
}
