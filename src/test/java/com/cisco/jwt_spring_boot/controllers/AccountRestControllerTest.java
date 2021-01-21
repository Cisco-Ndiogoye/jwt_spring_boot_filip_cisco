package com.cisco.jwt_spring_boot.controllers;

import com.cisco.jwt_spring_boot.entities.AppRole;
import com.cisco.jwt_spring_boot.entities.AppUser;
import com.cisco.jwt_spring_boot.entities.VerificationToken;
import com.cisco.jwt_spring_boot.entities.request.RegisterForm;
import com.cisco.jwt_spring_boot.exception.PasswordConfirmationException;
import com.cisco.jwt_spring_boot.services.AccountService;
import com.cisco.jwt_spring_boot.services.PasswordRecoverService;
import com.cisco.jwt_spring_boot.services.VerificationTokenServiceServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AccountRestControllerTest {

    private static AppUser appUser1;
    private static AppUser appUser2;
    private static AppUser appUser3;

    @Mock
    private AccountService accountService;

    @Mock
    private PasswordRecoverService passwordRecoverService;

    @Mock
    private VerificationTokenServiceServiceImpl verificationTokenServiceImpl;

    @InjectMocks
    private AccountRestController accountRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @BeforeAll
    public static void init() {

        Collection<AppRole> roles = new ArrayList<>();
        roles.add(new AppRole(null, "PHARMACIEN"));
        appUser1 = new AppUser("Gael", "12345", roles, "gael@example.com");
        appUser2 = new AppUser("Yolande", "1234", roles, "yolande@example.com");
        appUser3 = new AppUser("Adele", "1234", roles, "adele@example.com");

    }

    @Test
    @DisplayName("POST /api/account/register")
    void register() {

        RegisterForm registerForm = new RegisterForm("Gael", "12345", "12345", "gael@example.com", null, null, null, null, "+221000000000", "PHARMACIEN");
        AppUser registeredUser = accountRestController.register(registerForm);
        Mockito.verify(accountService, Mockito.times(1)).registerUser(registerForm);

    }

    @Test
    void updatePasswordValid() {

        RegisterForm registerForm = new RegisterForm("Gael", "1234", "1234", "gael@example.com", null, null, null, null, "+221000000000", "PHARMACIEN");
        appUser1 = accountRestController.updatePassword(registerForm);
        Mockito.verify(accountService, Mockito.times(1)).updateUserPassword(registerForm);


    }

    @Test
    void updatePasswordNotValid() throws PasswordConfirmationException {

        RegisterForm registerForm = new RegisterForm("Gael", "1234", "12345", "gael@example.com", null, null, null, null, "+221000000000", "PHARMACIEN");
        Assertions.assertThrows(PasswordConfirmationException.class, () ->{
           accountRestController.updatePassword(registerForm);
        });

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

    @Test
    void recover() {

        Mockito.when(passwordRecoverService.recoverPassword("example@example.com")).thenReturn(ResponseEntity.ok("Please check your mail address."));
        assertThat(accountRestController.recover("example@example.com").getStatusCode(), is(HttpStatus.OK));
        Mockito.verify(passwordRecoverService, Mockito.times(1)).recoverPassword("example@example.com");

    }

    @Test
    void verifyEmail() {

        VerificationToken verificationToken = new VerificationToken();
        Mockito.when(verificationTokenServiceImpl.verifyEmail(verificationToken.getToken())).thenReturn(ResponseEntity.ok("You have successfully verified your email address."));
        assertThat(accountRestController.verifyEmail(verificationToken.getToken()).getStatusCode(), is(HttpStatus.OK));
        Mockito.verify(verificationTokenServiceImpl, Mockito.times(1)).verifyEmail(verificationToken.getToken());

    }


    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}