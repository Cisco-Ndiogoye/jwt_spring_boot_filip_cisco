package com.cisco.jwt_spring_boot.services;

import com.cisco.jwt_spring_boot.dao.AppUserRepository;
import com.cisco.jwt_spring_boot.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PasswordRecoverService {


    private AppUserRepository userRepository;
    private SendMailService sendingMailService;

    @Autowired
    public PasswordRecoverService(AppUserRepository userRepository,SendMailService sendingMailService){
        this.sendingMailService = sendingMailService;
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> recoverPassword(String email){
        Optional<AppUser> appUser = userRepository.findByEmail(email);
        System.out.println(appUser);
        if(!appUser.isPresent()){
            return ResponseEntity.badRequest().body("No user linked to this email address.");
        }
        sendingMailService.sendPasswordRecoverMail(email);
        return ResponseEntity.ok("Please check your mail address.");

    }

}
