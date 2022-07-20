//package com.aaronr92.schoolwebservice.security;
//
////import com.aaronr92.schoolwebservice.filter.CustomAuthorizationFilter;
//import com.aaronr92.schoolwebservice.service.UserService;
//import com.aaronr92.schoolwebservice.util.Role;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import static org.springframework.http.HttpMethod.GET;
//import static org.springframework.http.HttpMethod.POST;
//
//@EnableWebSecurity
//public class WebSecurityConf2 extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    UserService userService;
//
//    @Autowired
//    BCryptPasswordEncoder passwordEncoder;
//
//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
//    }
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.httpBasic();
//        http.csrf().disable().headers().frameOptions().disable();
//        http.authorizeRequests()
//                .antMatchers("/api/login/**").permitAll()
//                .antMatchers(POST, "/api/auth/signup").permitAll()
//                .antMatchers(GET, "/api/user").hasAuthority(Role.ROLE_ADMINISTRATOR.name())
//                .anyRequest().authenticated();
//        http.sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//    }
//}
