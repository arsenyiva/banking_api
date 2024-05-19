package com.iva.banking_api.model.pojo;

import lombok.Data;


/**
 * Класс для передачи данных аутентификации.
 */
@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}
