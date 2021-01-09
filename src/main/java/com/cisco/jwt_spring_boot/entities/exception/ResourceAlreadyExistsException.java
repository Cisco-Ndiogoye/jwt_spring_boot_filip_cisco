package com.cisco.jwt_spring_boot.entities.exception;

public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(String msg){
        super(msg);
    }
}
