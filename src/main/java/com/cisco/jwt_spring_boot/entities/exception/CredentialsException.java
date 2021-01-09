package com.cisco.jwt_spring_boot.entities.exception;

public class CredentialsException extends RuntimeException {
    public CredentialsException(String msg) {
        super(msg);
    }
}
