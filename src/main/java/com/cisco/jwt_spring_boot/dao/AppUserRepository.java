package com.cisco.jwt_spring_boot.dao;

import com.cisco.jwt_spring_boot.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser,Long> {
    AppUser findByUsername(String username);
    AppUser findByEmail(String email);
}
