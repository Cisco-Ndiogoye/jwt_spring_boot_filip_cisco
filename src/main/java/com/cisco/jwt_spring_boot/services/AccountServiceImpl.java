package com.cisco.jwt_spring_boot.services;

import com.cisco.jwt_spring_boot.dao.AppRoleRepository;
import com.cisco.jwt_spring_boot.dao.AppUserRepository;
import com.cisco.jwt_spring_boot.entities.AppRole;
import com.cisco.jwt_spring_boot.entities.AppUser;
import com.cisco.jwt_spring_boot.entities.exception.PasswordConfirmationException;
import com.cisco.jwt_spring_boot.entities.exception.ResourceAlreadyExistsException;
import com.cisco.jwt_spring_boot.entities.exception.ResourceNotFoundException;
import com.cisco.jwt_spring_boot.entities.request.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private AppRoleRepository roleRepository;


    @Autowired
    private VerificationTokenService verificationTokenService;

    @Override
    public AppUser registerUser(RegisterForm userForm) {
        if(!userForm.getPassword().equals(userForm.getRepassword())) throw  new PasswordConfirmationException("Veuillez confirmer votre mot de passe.");
        if(this.emailExist(userForm.getEmail())) throw new ResourceAlreadyExistsException("Un compte est déjà lié à cette adresse email.");
        if(this.usernameExist(userForm.getUsername())) throw new ResourceAlreadyExistsException("Ce nom d'utilisateur existe déjà.");
        AppUser appUser = new AppUser(userForm);
        this.saveUser(appUser);
        if(userForm.getRole().equals("ADMIN")){
            this.addRoleToUser(userForm.getEmail(),"ADMIN");
            this.addRoleToUser(userForm.getEmail(), "PHARMACIEN");
        }if(userForm.getRole().equals("PHARMACIEN")){
            this.addRoleToUser(userForm.getEmail(), "PHARMACIEN");
        }
        verificationTokenService.createVerification(appUser.getEmail());
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
        if(user.get().getRoles() == null){
        List<AppRole> list = new ArrayList<AppRole>();
        list.add(role.get());
        user.get().setRoles(list);
        } else {
            user.get().getRoles().add(role.get());
        }
    }

    @Override
    public AppUser findUserByUsername(String username) {  return userRepository.findByUsername(username).get();    }

    @Override
    public AppUser findUserByEmail(String email){   return userRepository.findByEmail(email).get();    }

    @Override
    public AppUser updateUser(AppUser user) {
        Optional<AppUser> appUser = userRepository.findByEmail(user.getEmail());
        if(!appUser.isPresent()) throw new ResourceNotFoundException("Aucun utilisateur, avec ces informations, enregistré.");
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
