package com.cisco.jwt_spring_boot.dao;

import com.cisco.jwt_spring_boot.entities.AppRole;
import com.cisco.jwt_spring_boot.entities.AppUser;
import com.cisco.jwt_spring_boot.entities.VerificationToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@DataJpaTest
class VerificationTokenServiceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;


    @Test
    void findByUserEmail() {
        // given
        Collection<AppRole> roles = new ArrayList<>();
        roles.add(new AppRole(null,"ADMIN"));
        AppUser appUser = new AppUser("gael", "password", roles,"gael@example.com");
        entityManager.persist(appUser);
        entityManager.flush();

        // when

        List<VerificationToken> verificationTokens = verificationTokenRepository.findByUserEmail(appUser.getEmail());

        // then
        System.out.println(verificationTokens.get(0).getUser());
        assertThat(verificationTokens.get(0).getUser()).isEqualTo(appUser);
    }

    @Test
    void findByToken() {
    }
}