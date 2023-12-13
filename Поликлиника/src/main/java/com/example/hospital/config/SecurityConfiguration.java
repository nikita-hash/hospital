package com.example.hospital.config;


import com.example.hospital.handler.CustomAuthenticationFailureHandler;
import com.example.hospital.handler.LoginSuccessHandler;
import com.example.hospital.model.Role;
import com.example.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

@Configuration
@EnableWebSecurity
@EnableAspectJAutoProxy(proxyTargetClass=true)
@EnableMethodSecurity
public class SecurityConfiguration{

    String[] SEC_MATCHERS={"/registration/**","login","/login/**","/images/**"};

    @Autowired
    UserService userService;

    @Autowired
    CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    LoginSuccessHandler loginSuccessHandler;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName(null);
        http
                .csrf().disable()
                .requestCache((cache) -> cache
                        .requestCache(requestCache))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(SEC_MATCHERS)

                        .permitAll()
                        .requestMatchers("/admin/**").hasAnyAuthority(Role.ADMIN.getAuthority())
                        .requestMatchers("/doctor/**").hasAnyAuthority(Role.DOCTOR.getAuthority())
                        .requestMatchers("/patient/**").hasAnyAuthority(Role.PATIENT.getAuthority())
                        .requestMatchers("/visit/**").hasAnyAuthority(Role.DOCTOR.getAuthority())
                        .anyRequest()
                        .authenticated())
                .formLogin(login->login
                        .loginProcessingUrl("/perform-login")
                        .usernameParameter("login")
                        .passwordParameter("password")
                        .loginPage("/login")
                        .failureHandler(customAuthenticationFailureHandler)
                        .successHandler(loginSuccessHandler))
                .logout(logout->logout
                        .logoutUrl("/logout")
                )
                .cors(cors-> cors.disable()
                );
        return http.build();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        return provider;
    }



}