package com.cisco.jwt_spring_boot.controllers;

import com.cisco.jwt_spring_boot.dao.AppUserRepository;
import com.cisco.jwt_spring_boot.entities.AppUser;
import com.cisco.jwt_spring_boot.entities.request.RegisterForm;
import com.cisco.jwt_spring_boot.services.AccountService;
import com.cisco.jwt_spring_boot.services.PasswordRecoverService;
import com.cisco.jwt_spring_boot.services.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/register")
public class RegisterRestController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PasswordRecoverService passwordRecoverService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @GetMapping
    public String verifyEmail(@RequestParam String token) {
        return verificationTokenService.verifyEmail(token).getBody();
    }

    @PostMapping
    public AppUser register(@Valid @RequestBody RegisterForm userForm){
        System.out.println("**************************************************Registering**************************************************");
        if(!userForm.getPassword().equals(userForm.getRepassword())) throw  new RuntimeException("Veuillez confirmer votre mot de passe.");
        if(accountService.emailExist(userForm.getEmail())) throw new RuntimeException("Un compte est déjà lié à cette adresse email.");
        if(accountService.usernameExist(userForm.getUsername())) throw new RuntimeException("Ce nom d'utilisateur existe déjà.");
        AppUser appUser = new AppUser();
        appUser.setPassword(userForm.getPassword());
        appUser.setEmail(userForm.getEmail());
        appUser.setUsername(userForm.getUsername());
        accountService.saveUser(appUser);
        if(userForm.getRole() == "ADMIN"){
            accountService.addRoleToUser(userForm.getEmail(),"ADMIN");
            accountService.addRoleToUser(userForm.getEmail(), "PHARMACIEN");
        }if(userForm.getRole() == "PHARMACIEN"){
            accountService.addRoleToUser(userForm.getEmail(), "PHARMACIEN");
        }
        verificationTokenService.createVerification(appUser.getEmail());
        return appUser;
    }

}
