package com.cisco.jwt_spring_boot.entities;

import com.cisco.jwt_spring_boot.entities.request.RegisterForm;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class AppUser {

    @Id @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;

    @Column(name = "enabled")
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<AppRole> roles = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private VerificationToken verificationToken;



    public AppUser() {
        this.enabled = false;
    }

    public AppUser(RegisterForm userForm) {
        this.enabled = false;
        this.username = userForm.getUsername();
        this.password = userForm.getPassword();
        this.email = userForm.getEmail();
    }

    public AppUser(Long id, String username, String password, Collection<AppRole> roles,String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.email = email;
        this.enabled = false;
    }


    public AppUser(String username, String password, Collection<AppRole> roles, String email) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.email = email;
        this.enabled = false;
    }

    @JsonIgnore
    public VerificationToken getVerificationToken() {
        return verificationToken;
    }

    @JsonIgnore
    public void setVerificationToken(VerificationToken verificationToken) {
        this.verificationToken = verificationToken;
    }


    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonSetter
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Collection<AppRole> getRoles() {
        return roles;
    }

    public void setRoles(Collection<AppRole> roles) {
        this.roles = roles;
    }
}
