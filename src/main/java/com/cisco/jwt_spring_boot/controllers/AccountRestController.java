package com.cisco.jwt_spring_boot.controllers;

import com.cisco.jwt_spring_boot.dao.AppUserRepository;
import com.cisco.jwt_spring_boot.entities.AppUser;
import com.cisco.jwt_spring_boot.entities.request.RegisterForm;
import com.cisco.jwt_spring_boot.services.AccountService;
import com.cisco.jwt_spring_boot.services.PasswordRecoverService;
import com.cisco.jwt_spring_boot.services.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
        AppUser user = userRepository.findByEmail(userForm.getEmail());
        if (user == null) throw  new RuntimeException("Account credentials not updated.");
        if(!userForm.getPassword().equals(userForm.getRepassword())) throw  new RuntimeException("You must confirm your password");
        System.out.println(user.getPassword());
        System.out.println(userForm.getPassword());
        user.setPassword(userForm.getPassword());
        System.out.println(user.getPassword());
        return accountService.saveUser(user);
    }

    @PostMapping("/login/recover")
    public ResponseEntity<String> recover(@RequestBody String email){ return passwordRecoverService.recoverPassword(email); }

    @GetMapping("/users")
    public List<AppUser> users(){
        return accountService.allUsers();
    }

}
