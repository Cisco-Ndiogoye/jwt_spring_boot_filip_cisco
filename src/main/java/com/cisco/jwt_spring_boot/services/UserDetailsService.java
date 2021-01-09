package com.cisco.jwt_spring_boot.services;

import com.cisco.jwt_spring_boot.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        try {
            AppUser user = accountService.findUserByUsername(username);
            if(user == null) throw new UsernameNotFoundException(username);
            Collection<GrantedAuthority> authorities  = new ArrayList<>();
            user.getRoles().forEach(r-> {
                authorities.add(new SimpleGrantedAuthority(r.getRole()));
            });
            return new User(user.getUsername(),user.getPassword(),user.isEnabled(),accountNonExpired,credentialsNonExpired,accountNonLocked,authorities);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


}
