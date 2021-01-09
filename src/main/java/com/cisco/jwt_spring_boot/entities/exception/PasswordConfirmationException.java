package com.cisco.jwt_spring_boot.entities.exception;

public class PasswordConfirmationException extends RuntimeException {
    public PasswordConfirmationException(String message) {
        super(message);
    }
}
