package com.cisco.jwt_spring_boot.controllers;

import com.cisco.jwt_spring_boot.entities.AppRole;
import com.cisco.jwt_spring_boot.entities.AppUser;
import com.cisco.jwt_spring_boot.entities.request.RegisterForm;
import com.cisco.jwt_spring_boot.services.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.doReturn;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class AccountRestControllerTest {

    private static AppUser appUser1;
    private static AppUser appUser2;
    private static AppUser appUser3;

    @MockBean
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void init() {

        Collection<AppRole> roles = new ArrayList<>();
        roles.add(new AppRole(null, "PHARMACIEN"));
        //appUser1 = new AppUser("Gael", "12345", roles, "gael@example.com");
        appUser2 = new AppUser("Yolande", "1234", roles, "yolande@example.com");
        appUser3 = new AppUser("Adele", "1234", roles, "adele@example.com");

    }

    @Test
    @DisplayName("POST /api/account/register")
    void registerWithoutJWTToken() throws Exception {

        RegisterForm registerForm = new RegisterForm("Gael", "1234", "1234", "gael@example.com", null, null, null, null, "+221000000000", "PHARMACIEN");
        doReturn(appUser1).when(accountService).registerUser(registerForm);


        mockMvc.perform(post("/api/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(registerForm)))

                .andExpect(status().isForbidden());
    }

    /*
    @Test
    void updatePasswordValid() {

        RegisterForm registerForm = new RegisterForm("Gael", "1234", "1234", "gael@example.com", null, null, null, null, "+221000000000", "PHARMACIEN");
        appUser1 = accountRestController.updatePassword(registerForm);
        assertThat(accountService.findUserByUsername(registerForm.getUsername()).getPassword(), is(registerForm.getPassword()));

    }

    @Test
    void updatePasswordNotValid() {

        RegisterForm registerForm = new RegisterForm("Gael", "1234", "12345", "gael@example.com", null, null, null, null, "+221000000000", "PHARMACIEN");
        assertThat(accountRestController.updatePassword(registerForm), instanceOf(RuntimeException.class));


    }


    @Test
    void recover() {
    }

    @Test
    void verifyEmail() {
    }


    @Test
    void users_WhenNoRecord() {

        Mockito.when(accountService.allUsers()).thenReturn(Arrays.asList());
        assertThat(accountRestController.users().size(), is(0));
        Mockito.verify(accountService, Mockito.times(1)).allUsers();

    }

    @Test
    void users_WhenRecord() {

        Mockito.when(accountService.allUsers()).thenReturn(Arrays.asList(appUser3,appUser2));
        assertThat(accountRestController.users().size(), is(2));
        Mockito.verify(accountService, Mockito.times(1)).allUsers();

    }

     */

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}