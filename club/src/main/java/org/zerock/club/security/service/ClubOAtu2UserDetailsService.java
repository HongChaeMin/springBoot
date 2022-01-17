package org.zerock.club.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.entity.ClubMemberRole;
import org.zerock.club.repository.ClubMemberRepository;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubOAtu2UserDetailsService  extends DefaultOAuth2UserService {

    private final ClubMemberRepository clubMemberRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("-------------------------------------------------");
        log.info("userRequest : " + userRequest);

        String clientName = userRequest.getClientRegistration().getClientName();

        log.info("-------------------------------------------------");
        log.info("clientName : " + clientName);
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // properties에 email을 설정해 주어서 email 정보 가지고 옴
        log.info("=================================================");
        oAuth2User.getAttributes().forEach((k, v) -> {
            log.info(k + " : " + v);
        });

        String email = null;

        if (clientName.equals("Google")) { // 구글을 이용하는 경우
            email = oAuth2User.getAttribute("email");
        }

        log.info("EMAIL : " + email);

        // ClubMember clubMember = insertSocialMember(email);
        // return oAuth2User;

        ClubMember member = insertSocialMember(email);

        ClubAuthMemberDTO clubAuthMemberDTO = new ClubAuthMemberDTO(
            member.getId(),
            member.getEmail(),
            member.getName(),
            member.getPassword(),
            true,
            member.getRoleSet().stream().map(clubMemberRole ->
                new SimpleGrantedAuthority("ROLE_" + clubMemberRole.name())).collect(Collectors.toList()),
                oAuth2User.getAttributes()
        );

        return clubAuthMemberDTO;
    }

    private ClubMember insertSocialMember(String email) {

        Optional<ClubMember> result = clubMemberRepository.findByEmail(email, true);

        // 기존에 동일한 이메일로 가입한 회원이 있는 경우에는 조회 후 그대로 리턴
        if (result.isPresent()) {
            return result.get();
        }

        // 없다면 회원 추가
        ClubMember clubMember = ClubMember.builder()
                .email(email)
                .password(passwordEncoder.encode("1111")) // 얘는 어떡하지...?;; 나중에 생각 ㄱ
                .fromSocial(true)
                .build();
        clubMember.addMemberRole(ClubMemberRole.USER);

        clubMemberRepository.save(clubMember);

        return clubMember;

    }

}
