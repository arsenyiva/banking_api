package com.iva.banking_api.controller;

import com.iva.banking_api.service.BankAccountService;
import com.iva.banking_api.service.RegistrationService;
import com.iva.banking_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;


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
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.badRequest().body("Phone, or email is already taken");
        }
    }
}
