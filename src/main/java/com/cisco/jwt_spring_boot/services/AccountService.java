package com.cisco.jwt_spring_boot.services;

import com.cisco.jwt_spring_boot.entities.AppRole;
import com.cisco.jwt_spring_boot.entities.AppUser;

import java.util.List;

public interface AccountService {

    AppUser saveUser(AppUser user);
    AppRole saveRole(AppRole role);
    void addRoleToUser(String username, String role);
    AppUser findUserByUsername(String username);
    AppUser findUserByEmail(String email);
    List<AppUser> allUsers();
    boolean emailExist(String email);
    boolean usernameExist(String username);
}
