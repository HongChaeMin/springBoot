package org.zerock.club.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.entity.ClubMemberRole;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ClubMemberTests {

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Test
    public void insertDummies() {

        // 1 - 80 : USER
        // 81 - 90 : USER, MANAGER
        // 91 - 100 : USER, MANAGER, ADMIN

        IntStream.rangeClosed(101, 106).forEach(i -> {
            ClubMember clubMember = ClubMember.builder()
                    .email("user" + i + "@minHong.com")
                    .name("사용자" + i)
                    .fromSocial(false)
                    .password(passwordEncoder.encode("1111"))
                    .build();

            clubMember.addMemberRole(ClubMemberRole.USER);
            clubMember.addMemberRole(ClubMemberRole.MANAGER);
            clubMember.addMemberRole(ClubMemberRole.ADMIN);

            clubMemberRepository.save(clubMember);
        });
    }

    @Test
    public void selectByIdAndSocial() {
        Optional<ClubMember> result = clubMemberRepository.findByEmail("user95@minHong.com", false);

        ClubMember clubMember = result.get();

        System.out.println(clubMember);
    }

}
