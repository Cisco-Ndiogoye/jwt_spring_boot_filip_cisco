package com.cisco.jwt_spring_boot.services;

import com.cisco.jwt_spring_boot.dao.AppRoleRepository;
import com.cisco.jwt_spring_boot.dao.AppUserRepository;
import com.cisco.jwt_spring_boot.entities.AppRole;
import com.cisco.jwt_spring_boot.entities.AppUser;
import com.cisco.jwt_spring_boot.exception.PasswordConfirmationException;
import com.cisco.jwt_spring_boot.exception.ResourceAlreadyExistsException;
import com.cisco.jwt_spring_boot.exception.ResourceNotFoundException;
import com.cisco.jwt_spring_boot.entities.request.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private static final String USER_NOT_FOUND = "Aucun utilisateur, avec ces informations, enregistré.";
    private static final String PASSWORD_CONFIRMATION_ERROR = "Veuillez confirmer votre mot de passe.";
    private static final String EMAIL_ALREADY_EXISTS = "Un compte est déjà lié à cette adresse email.";
    private static final String USERNAME_ALREADY_EXITS = "Ce nom d'utilisateur existe déjà.";
    private static final String ADMIN_ROLE = "ADMIN";
    private static final String PHARMACIEN_ROLE = "PHARMACIEN";

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private AppRoleRepository roleRepository;


    @Autowired
    private VerificationTokenServiceServiceImpl verificationTokenServiceImpl;

    @Override
    public AppUser registerUser(RegisterForm userForm) {
        if(!userForm.getPassword().equals(userForm.getRepassword())) throw  new PasswordConfirmationException(PASSWORD_CONFIRMATION_ERROR);
        if(this.emailExist(userForm.getEmail())) throw new ResourceAlreadyExistsException(EMAIL_ALREADY_EXISTS);
        if(this.usernameExist(userForm.getUsername())) throw new ResourceAlreadyExistsException(USERNAME_ALREADY_EXITS);
        AppUser appUser = new AppUser(userForm);
        this.saveUser(appUser);
        if(userForm.getRole().equals(ADMIN_ROLE)){
            this.addRoleToUser(userForm.getEmail(), ADMIN_ROLE);
            this.addRoleToUser(userForm.getEmail(), PHARMACIEN_ROLE);
        }
        if(userForm.getRole().equals(PHARMACIEN_ROLE)){
            this.addRoleToUser(userForm.getEmail(), PHARMACIEN_ROLE);
        }
        verificationTokenServiceImpl.createVerification(appUser.getEmail());
        return appUser;
    }

    @Override
    public AppUser saveUser(AppUser user) {
        String hashPass = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(hashPass);
        return userRepository.save(user);
    }

    @Override
    public AppRole saveRole(AppRole role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String email, String roleName) {
        Optional<AppRole> role = roleRepository.findByRole(roleName);
        Optional<AppUser> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            if (!role.isPresent()) {
                user.get().getRoles().add(this.saveRole(new AppRole(null, roleName)));
            } else {
                user.get().getRoles().add(role.get());
            }
        } else {
            throw new ResourceNotFoundException(USER_NOT_FOUND);
        }
    }

    @Override
    public AppUser findUserByUsername(String username) {
        Optional<AppUser> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new ResourceNotFoundException(USER_NOT_FOUND);
        }
    }

    @Override
    public AppUser findUserByEmail(String email){
        Optional<AppUser> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new ResourceNotFoundException(USER_NOT_FOUND);
        }
    }

    @Override
    public AppUser updateUser(AppUser user) {
        Optional<AppUser> appUser = userRepository.findByEmail(user.getEmail());
        if(!appUser.isPresent()) throw new ResourceNotFoundException(USER_NOT_FOUND);
        return userRepository.save(user);
    }

    @Override
    public AppUser updateUserPassword(RegisterForm registerForm) {
        AppUser user = this.findUserByEmail(registerForm.getEmail());
        user.setPassword(registerForm.getPassword());
        return this.updateUser(user);
    }


    @Override
    public List<AppUser> allUsers() {   return userRepository.findAll();    }

    @Override
    public boolean emailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean usernameExist(String username) {
        return userRepository.findByUsername(username).isPresent();
    }


}
