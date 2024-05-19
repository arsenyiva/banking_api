package com.iva.banking_api.model.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс для передачи данных аутентификации.
 */
@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}
