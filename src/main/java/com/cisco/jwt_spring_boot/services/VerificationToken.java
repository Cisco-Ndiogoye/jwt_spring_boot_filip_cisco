package com.cisco.jwt_spring_boot.services;

import org.springframework.http.ResponseEntity;

public interface VerificationToken {
    void createVerification(String email);
    ResponseEntity<String> verifyEmail(String token);
}
