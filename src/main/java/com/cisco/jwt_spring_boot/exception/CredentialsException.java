package com.cisco.jwt_spring_boot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class CredentialsException extends RuntimeException {
    public CredentialsException(String msg) {
        super(msg);
    }
}
