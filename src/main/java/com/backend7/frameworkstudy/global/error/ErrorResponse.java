package com.backend7.frameworkstudy.global.error;

import com.backend7.frameworkstudy.global.common.ApiResultType;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ErrorResponse {

    private int code;
    private HttpStatus status;
    private String message;

    private ErrorResponse(HttpStatus status, String message) {
        this.code = status.value();
        this.status = status;
        this.message = message;
    }

    public static ResponseEntity<ErrorResponse> of(ApiResultType failType) {
        return ResponseEntity.status(failType.getStatus())
                .body(new ErrorResponse(failType.getStatus(), failType.getMessage()));
    }
}
