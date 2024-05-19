package com.iva.banking_api.controller;


import com.iva.banking_api.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

/**
 * Контроллер для регистрации новых пользователей.
 */
@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;
    private static final org.apache.log4j.Logger log = Logger.getLogger(AuthController.class);

    /**
     * Регистрирует нового пользователя.
     *
     * @param requestData данные запроса, содержащие информацию о новом пользователе
     * @return ResponseEntity с сообщением о результате операции
     */
    @Operation(summary = "Регистрация нового пользователя",
            description = "Регистрирует нового пользователя с указанными данными.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Невозможно зарегистрировать пользователя, телефон или электронная почта уже заняты")
    })
    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody Map<String, Object> requestData) {
        String username = (String) requestData.get("username");
        String password = (String) requestData.get("password");
        LocalDate dateOfBirth = LocalDate.parse((String) requestData.get("dateOfBirth"));
        String phone = (String) requestData.get("phone");
        String extraPhone = (String) requestData.get("extraPhone");
        String email = (String) requestData.get("email");
        Double initialBalance = (Double) requestData.get("initialBalance");
        if (initialBalance < 0) {
            return ResponseEntity.badRequest().body("Balance cannot be less zero");
        }
        if (registrationService.registerNewUser(username, password, dateOfBirth, phone, extraPhone, email, initialBalance)) {
            log.debug(username + " registered successfully");
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.badRequest().body("Phone, or email is already taken");
        }
    }
}