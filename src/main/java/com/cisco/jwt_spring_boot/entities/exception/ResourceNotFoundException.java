package com.cisco.jwt_spring_boot.entities.exception;

public class ResourceNotFoundException extends RuntimeException {

    public  ResourceNotFoundException(String msg) {
        super(msg);
    }
}
