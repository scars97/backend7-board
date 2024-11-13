package com.backend7.frameworkstudy.global.common;

import org.springframework.http.HttpStatus;

public interface ApiResultType {

    HttpStatus getStatus();

    String getMessage();
}
