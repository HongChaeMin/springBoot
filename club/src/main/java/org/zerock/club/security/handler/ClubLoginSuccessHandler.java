package org.zerock.club.security.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class ClubLoginSuccessHandler implements AuthenticationSuccessHandler {

    // 소셜 로그인한 사용자의 패스워드를 변경, 사용자의 이름을 지정 등의 작업을 수행할 수 있도록 함

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private PasswordEncoder passwordEncoder;

    public ClubLoginSuccessHandler(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("------------------------");
        log.info("onAuthenticationSuccess");
        log.info("------------------------");

        ClubAuthMemberDTO authMemberDTO = (ClubAuthMemberDTO) authentication.getPrincipal();

        boolean fromSocial = authMemberDTO.isFromSocial();

        log.info("Need Modify Member ? " + fromSocial);

        boolean passwordResult = passwordEncoder.matches("1111", authMemberDTO.getPassword());

        if (fromSocial && passwordResult) {
            redirectStrategy.sendRedirect(request, response, "/member/modify?from=social");
        }

    }
}
