package com.cisco.jwt_spring_boot.services;

public interface SendMail {
    boolean sendVerificationMail(String toEmail, String verificationCode);
    boolean sendUserCredentials(String toEmail, String username, String password);
    void sendEmail(String toEmail, String subject, String body);
}
