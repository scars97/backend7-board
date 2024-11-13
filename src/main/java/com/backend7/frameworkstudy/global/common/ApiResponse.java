package com.backend7.frameworkstudy.global.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private int code;
    private HttpStatus status;
    private String message;
    private T data;

    private ApiResponse(HttpStatus status, String message, T data) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    private ApiResponse(HttpStatus status, String message) {
        this.code = status.value();
        this.status = status;
        this.message = message;
    }

    public static <T> ApiResponse<T> ok(ApiResultType successType, T data) {
        return new ApiResponse<>(successType.getStatus(), successType.getMessage(), data);
    }

    public static <T> ApiResponse<T> ok(ApiResultType successType) {
        return new ApiResponse<>(successType.getStatus(), successType.getMessage());
    }

    public static <T> ApiResponse<T> fail(ApiResultType failType) {
        return new ApiResponse<>(failType.getStatus(), failType.getMessage());
    }
}
