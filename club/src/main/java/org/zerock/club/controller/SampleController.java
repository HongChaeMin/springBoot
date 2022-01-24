package org.zerock.club.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

@Controller
@Log4j2
@RequestMapping("/sample/")
public class SampleController {

    @PreAuthorize("permitAll()")
    @GetMapping("all")
    public void exAll() {
        log.info("exAll....................");
    }

    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("member")
    public void exMember(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMemberDTO) {
        log.info("exMember.................");

        log.info("---------------------------------------");
        log.info(clubAuthMemberDTO);
        log.info("---------------------------------------");

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("admin")
    public void exAdmin() {
        log.info("exAdmin..................");
    }

    // 특별히 정해진 사용자만 처리하고 싶은 경우
    @PreAuthorize("#clubAuthMember != null && #clubAuthMember.userName eq \"user95@minHong.com\"")
    @GetMapping("/exOnly")
    public String exMemberOnly(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMemberDTO) {
        log.info("exMemberOnly............................");
        log.info(clubAuthMemberDTO);

        return "/sample/admin";
    }

}
