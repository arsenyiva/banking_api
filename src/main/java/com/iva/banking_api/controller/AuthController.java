package com.iva.banking_api.controller;

import com.iva.banking_api.model.pojo.AuthenticationRequest;
import com.iva.banking_api.model.pojo.AuthenticationResponse;
import com.iva.banking_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер аутентификации.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final org.apache.log4j.Logger log = Logger.getLogger(AuthController.class);


    @Autowired
    private AuthService authService;

    /**
     * Создает JWT токен для аутентификации пользователя.
     *
     * @param authenticationRequest Запрос на аутентификацию
     * @return Ответ с JWT токеном или сообщением об ошибке
     */
    @PostMapping
    @Operation(summary = "Аутентификация пользователя и генерация JWT токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            String token = authService.authenticateAndGetToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            log.debug(authenticationRequest.getUsername() + " successfully authenticated");
            return ResponseEntity.ok(new AuthenticationResponse(token));
        } catch (AuthenticationException e) {
            log.error("Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}