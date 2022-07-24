package com.aaronr92.schoolwebservice.security;

import com.aaronr92.schoolwebservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static com.aaronr92.schoolwebservice.util.Role.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint);
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("login/**").permitAll()
                .antMatchers(POST, "/api/user/add").hasAuthority(ROLE_ADMINISTRATOR.name())
                .antMatchers(GET, "/api/user").hasAuthority(ROLE_ADMINISTRATOR.name())
                .antMatchers(DELETE, "/api/user").hasAuthority(ROLE_ADMINISTRATOR.name())
                .antMatchers(PUT, "/api/user/change/password").authenticated()
                .anyRequest().permitAll();      // for testing purposes
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }
}
