package org.zerock.club.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 1단계
    // 사용자가 원하는 URL을 입력합니다

    // 2단계
    // 스프링 시큐리티에서는 인증/인가가 필요하다고 판단하고(필터에서 판단) 사용자가 인증하도록 로그인 화면을 보여줍니다

    // 3단계
    // 정보가 전달된다면 AuthenticationManager가 적절한 AuthenticationProvider를 찾아서 인증을 시도합니다.

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/sample/all").permitAll() // 모든 사용자 허용
                .antMatchers("/sample/member").hasRole("USER") // USER 권한을 가진 사용자만 허용
                .antMatchers("/sample/admin").hasRole("ADMIN");

        http.formLogin(); // 인가/인증에 문제시 로그인 화면
        http.csrf().disable(); // csrf 토큰 비활성화

        http.oauth2Login();

        // csrf 토큰을 활성화 했을때는 post 방식으로 로그아웃, 비활성화시에는 get 방식으로 로그아웃 처리 가능
        // http.logout();

        // 별도의 로그인 페이지를 이용
        // http.fromPage();
    }

    // 더이상 사용하지 않음
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user1")
                .password("$2a$10$6jbvDd0TJqKa.lA1BE0Lnuv500XHJyh2DQUL.I2vYbh/HczYdW8Me")
                .roles("USER");
    }*/

}
