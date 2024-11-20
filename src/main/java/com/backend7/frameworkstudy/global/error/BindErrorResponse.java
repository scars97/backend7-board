package com.backend7.frameworkstudy.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Getter
public class BindErrorResponse {

    private int code;
    private HttpStatus status;
    private Map<String, String> reason;

    private BindErrorResponse(HttpStatus status, Map<String, String> reason) {
        this.code = status.value();
        this.status = status;
        this.reason = reason;
    }

    public static ResponseEntity<BindErrorResponse> of(HttpStatus status, Map<String, String> reason) {
        return ResponseEntity.status(status).body(new BindErrorResponse(status, reason));
    }
}
