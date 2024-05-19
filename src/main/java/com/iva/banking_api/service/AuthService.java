package com.iva.banking_api.service;

import com.iva.banking_api.util.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Сервис аутентификации пользователя и генерации JWT-токена.
 */
@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Аутентифицирует пользователя и генерирует JWT-токен для него.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     * @return JWT-токен для аутентифицированного пользователя
     */
    public String authenticateAndGetToken(String username, String password) {
        authenticate(username, password);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtTokenUtils.generateToken(userDetails);
    }

    /**
     * Выполняет аутентификацию пользователя.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     */
    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}