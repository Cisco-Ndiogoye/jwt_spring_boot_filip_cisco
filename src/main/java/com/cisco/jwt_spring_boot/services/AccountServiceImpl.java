package com.cisco.jwt_spring_boot.services;

import com.cisco.jwt_spring_boot.dao.AppRoleRepository;
import com.cisco.jwt_spring_boot.dao.AppUserRepository;
import com.cisco.jwt_spring_boot.entities.AppRole;
import com.cisco.jwt_spring_boot.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


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
        AppRole role = roleRepository.findByRole(roleName);
        AppUser user = userRepository.findByEmail(email);
        if(user.getRoles() == null){
        List<AppRole> list = new ArrayList<AppRole>();
        list.add(role);
        user.setRoles(list);
        } else {
            user.getRoles().add(role);
        }
    }

    @Override
    public AppUser findUserByUsername(String username) {  return userRepository.findByUsername(username);    }

    @Override
    public AppUser findUserByEmail(String email){   return userRepository.findByEmail(email);    }

    @Override
    public List<AppUser> allUsers() {   return userRepository.findAll();    }

    @Override
    public boolean emailExist(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public boolean usernameExist(String username) {
        return userRepository.findByUsername(username) != null;
    }


}
