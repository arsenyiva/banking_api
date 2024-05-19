package com.iva.banking_api.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * Класс для передачи ответа на запрос аутентификации.
 * Содержит JWT токен, который используется для доступа к защищенным ресурсам.
 */
@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
}

