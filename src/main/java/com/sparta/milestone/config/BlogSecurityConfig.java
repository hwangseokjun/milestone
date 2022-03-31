package com.sparta.milestone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(prePostEnabled = true) // prePost 기능 사용
public class BlogSecurityConfig extends WebSecurityConfigurerAdapter {

    // 등록을 하지 않고 작동시켜 봤다. 어떤 차이가 있는가? 작동 자체가 안됨
    @Bean
    public BCryptPasswordEncoder encodePassword() { // BCryptPasswordEncoder 은 제공되어짐
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
// 회원 관리 처리 API (POST /user/**) 에 대해 CSRF 무시
        http.csrf()
                .ignoringAntMatchers("/members/**");

        http.authorizeRequests()
// image 폴더를 login 없이 허용
                .antMatchers("/images/**").permitAll()
// css 폴더를 login 없이 허용
                .antMatchers("/css/**").permitAll()
// 회원 관리 처리 API 전부를 login 없이 허용
                .antMatchers("/members/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/api/blogs").permitAll()
                .antMatchers("/blogs/details/**").permitAll()
// 그 외 어떤 요청이든 '인증'
                .anyRequest().authenticated()
                .and()
// 로그인 기능
                // [로그인 기능]
                .formLogin()
                // 로그인 View 제공 (GET /members/login)
                .loginPage("/members/deny")
                // 로그인 처리 (POST /members/login)
                .loginProcessingUrl("/members/login")
                // 로그인 처리 후 성공 시 URL
                .defaultSuccessUrl("/", true)
                // 로그인 처리 후 실패 시 URL
                .failureUrl("/members/login?error").permitAll()
                .and()
                // [로그아웃 기능]
                .logout()
                // 로그아웃 처리 URL
                .logoutUrl("/members/logout")
                .logoutSuccessUrl("/members/successLogout").permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/forbiden.html");
    }
}
