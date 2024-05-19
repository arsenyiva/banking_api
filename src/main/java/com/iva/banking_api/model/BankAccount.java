package com.iva.banking_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Модель, представляющая банковский счет.
 * Содержит информацию о владельце счета и его балансе.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bank_accounts")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @Min(value = 0)
    private Double balance;
}
