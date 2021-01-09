package com.cisco.jwt_spring_boot;

import com.cisco.jwt_spring_boot.entities.*;
import com.cisco.jwt_spring_boot.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyRunner implements CommandLineRunner {

    @Autowired
    private AccountService accountService;


    @Override
    public void run(String... args) throws Exception {

        accountService.saveRole(new AppRole(null,"ADMIN"));
        accountService.saveRole(new AppRole(null,"USER"));



        AppUser user1 = new AppUser("admin","1234",null, "admin@admin.com");
        AppUser user2 = new AppUser("user","1234",null, "user@admin.com");
        user1.setEnabled(true);
        user2.setEnabled(true);
        accountService.saveUser(user1);
        accountService.saveUser(user2);
        accountService.addRoleToUser("admin@admin.com","ADMIN");
        accountService.addRoleToUser("admin@admin.com","USER");
        accountService.addRoleToUser("user@admin.com","USER");


    }
}
