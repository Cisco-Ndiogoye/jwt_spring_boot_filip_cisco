package com.cisco.jwt_spring_boot.dao;

import com.cisco.jwt_spring_boot.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
    Optional<AppRole> findByRole(String role);
}
