package com.cisco.jwt_spring_boot.dao;

import com.cisco.jwt_spring_boot.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
    AppRole findByRole(String role);
}
