package com.cisco.jwt_spring_boot.entities.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data @NoArgsConstructor @AllArgsConstructor
public class RegisterForm {

    @NotEmpty
    private String username;
    @NotNull
    @NotEmpty
    private String password;
    private String repassword;
    @NotEmpty(message = "Veuillez renseigner votre adresse email")
    @Email(message = "Veuillez renseigner une adresse email valide")
    private String email;
    private String firstname;
    private String lastname;
    private String company;
    private String address;
    @NotNull
    private String phone;
    @NotNull
    private String role;
}
