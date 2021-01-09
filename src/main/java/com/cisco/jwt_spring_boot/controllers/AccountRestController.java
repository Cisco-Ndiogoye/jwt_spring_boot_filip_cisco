package com.cisco.jwt_spring_boot.controllers;

import com.cisco.jwt_spring_boot.dao.AppUserRepository;
import com.cisco.jwt_spring_boot.entities.AppUser;
import com.cisco.jwt_spring_boot.entities.exception.PasswordConfirmationException;
import com.cisco.jwt_spring_boot.entities.request.RegisterForm;
import com.cisco.jwt_spring_boot.services.AccountService;
import com.cisco.jwt_spring_boot.services.PasswordRecoverService;
import com.cisco.jwt_spring_boot.services.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountRestController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PasswordRecoverService passwordRecoverService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @PutMapping("/login/recover")
    public AppUser updatePassword ( @RequestBody RegisterForm userForm){
        if(!userForm.getPassword().equals(userForm.getRepassword())) throw  new PasswordConfirmationException("Veuillez confirmer votre mot de passe.");
        System.out.println(userForm.getPassword());
        System.out.println(userForm.getPassword());
        return accountService.updateUserPassword(userForm);
    }

    @PostMapping("/login/recover")
    public ResponseEntity<String> recover(@RequestBody String email){ return passwordRecoverService.recoverPassword(email); }

    @GetMapping("/register/verify")
    public String verifyEmail(@RequestParam String token) {
        return verificationTokenService.verifyEmail(token).getBody();
    }

    @PostMapping("/register")
    public AppUser register(@Valid @RequestBody RegisterForm userForm){
        return accountService.registerUser(userForm);
    }

    @GetMapping
    public List<AppUser> users(){
        return accountService.allUsers();
    }

}
