package com.cisco.jwt_spring_boot.services;

import com.cisco.jwt_spring_boot.entities.AppRole;
import com.cisco.jwt_spring_boot.entities.AppUser;
import com.cisco.jwt_spring_boot.entities.request.RegisterForm;

import java.util.List;

public interface AccountService {

    AppUser registerUser(RegisterForm userForm);
    AppUser saveUser(AppUser user);
    AppRole saveRole(AppRole role);
    void addRoleToUser(String username, String role);
    AppUser findUserByUsername(String username);
    AppUser findUserByEmail(String email);
    AppUser updateUser(AppUser user);
    AppUser updateUserPassword(RegisterForm registerForm);
    List<AppUser> allUsers();
    boolean emailExist(String email);
    boolean usernameExist(String username);
}
