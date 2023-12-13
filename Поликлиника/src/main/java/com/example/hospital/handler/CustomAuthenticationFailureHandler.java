package com.example.hospital.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        if (exception instanceof UsernameNotFoundException) {
            String notFound = "Аккаунт не был найден !";
            request.getSession().setAttribute("errorMessage",notFound);
            response.sendRedirect("/login?error");
        } else if (exception instanceof LockedException) {
            String lockedMessage = "Аккаунт заблокирован !";
            request.getSession().setAttribute("errorMessage",lockedMessage);
            response.sendRedirect("/login?error");
        } else{
            // Обработка других типов ошибок, если необходимо
            String notFound = "Аккаунт заблокирован !";
            request.getSession().setAttribute("errorMessage",notFound);
            response.sendRedirect("/login?error");
        }
    }
}
