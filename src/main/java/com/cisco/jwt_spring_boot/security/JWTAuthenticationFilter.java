package com.cisco.jwt_spring_boot.security;

import com.cisco.jwt_spring_boot.entities.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super();
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/api/account/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        AppUser user = null;
        try{
            user = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword())
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        try{
            User springUser = (User) authResult.getPrincipal();
            String jwtToken = Jwts.builder()
                    .setSubject(springUser.getUsername())
                    .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512,SecurityConstants.SECRET.getBytes(StandardCharsets.UTF_8))
                    .claim("roles",springUser.getAuthorities())
                    .compact();
            response.addHeader(SecurityConstants.HEADER_STRING,SecurityConstants.TOKEN_PREFIX + jwtToken);
            //System.out.println(jwtToken);
        }catch (AuthenticationException e){
            unsuccessfulAuthentication(request,response,e);
        }

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "Nom d'utilisateur ou mot de passe incorrect";

        if (exception.getMessage().equalsIgnoreCase("User is disabled")) {
            errorMessage = "Veuillez activer votre compte";
        } else if (exception.getMessage().equalsIgnoreCase("User account has expired")) {
            errorMessage = "Ce compte utilisateur a expiré";
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.getWriter().print(errorMessage);
        response.getWriter().flush();
    }


}
