package com.backend7.frameworkstudy.global.error;

import com.backend7.frameworkstudy.domain.board.exception.BoardException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<BindErrorResponse> validationExceptionHandler(BindException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            log.error("Invalid Input : {} - {}", fieldName, errorMessage);
        });
        return BindErrorResponse.of(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(BoardException.class)
    public ResponseEntity<ErrorResponse> boardExceptionHandler(BoardException exception) {
        log.error("Board Exception occurred : {}", exception.getErrorType());
        return ErrorResponse.of(exception.getErrorType());
    }
}
