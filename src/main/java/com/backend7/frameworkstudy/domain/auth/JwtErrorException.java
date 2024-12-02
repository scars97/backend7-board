package com.backend7.frameworkstudy.domain.auth;

import org.springframework.security.core.AuthenticationException;

public class JwtErrorException extends AuthenticationException {

    public JwtErrorException(String message) {
        super(message);
    }

}
