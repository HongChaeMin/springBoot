package org.zerock.club.security.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Log4j2
@Setter
@Getter
public class ClubAuthMemberDTO extends User {

    private Long id;

    private String email;

    private String name;

    private boolean fromSocial;

    // 부모 클래스를 사용하므로 별도의 멤버 변수로 선언하지 않음
    // private String password;

    public ClubAuthMemberDTO(
            Long id,
            String username,
            String name,
            String password,
            boolean fromSocial,
            Collection<? extends GrantedAuthority> authorities) {

        // 부모 클래스의 User 클래스에 사용자 정의 생성자가 있으므로 반드시 호출
        super(username, password, authorities);

        this.id = id;
        this.email = username;
        this.name = name;
        this.fromSocial = fromSocial;

    }

}
