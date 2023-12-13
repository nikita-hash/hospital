package com.example.hospital.handler;

import com.example.hospital.dto.UserSingInDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ADMIN")) {
            response.sendRedirect("/admin/"+((UserSingInDto)authentication.getPrincipal()).getId());
        }
        if(roles.contains("PATIENT")){
            response.sendRedirect("/patient/"+((UserSingInDto)authentication.getPrincipal()).getId());
        }
        if(roles.contains("DOCTOR")){
            response.sendRedirect("/doctor/"+((UserSingInDto)authentication.getPrincipal()).getId());
        }
    }
}
