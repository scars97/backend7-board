package com.backend7.frameworkstudy.global.error;

import com.backend7.frameworkstudy.domain.board.exception.BoardException;
import com.backend7.frameworkstudy.global.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BoardException.class)
    public ApiResponse<?> boardExceptionHandler(BoardException exception) {
        log.error("Board Exception occurred : {}", exception.getErrorType());
        return ApiResponse.fail(exception.getErrorType());
    }
}
