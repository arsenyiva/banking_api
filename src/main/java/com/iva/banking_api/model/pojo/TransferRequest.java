package com.iva.banking_api.model.pojo;

import lombok.Data;

/**
 * Класс для передачи данных о переводе денег.
 * Содержит идентификатор получателя и сумму перевода.
 */
@Data
public class TransferRequest {
    private Long receiverId;
    private Double amount;
}
