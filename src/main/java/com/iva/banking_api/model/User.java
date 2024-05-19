package com.iva.banking_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Модель, представляющая пользователя.
 * Содержит информацию о пользователе, такую как имя пользователя, пароль, дату рождения и контактные данные.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @Column(name = "date_of_birth")
    @NotNull
    private LocalDate dateOfBirth;

    @Column(unique = true)
    @Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$", message = "Number should be valid!")
    private String phone;

    @Column(unique = true)
    @Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$", message = "Number should be valid!")
    private String extraPhone;

    @NotNull
    @Column(unique = true)
    @Email(message = "Email should be valid!")
    private String email;

    @Column(name = "extra_email", unique = true)
    @Email(message = "Email should be valid!")
    private String extraEmail;

    @OneToOne(mappedBy = "owner", cascade = CascadeType.ALL)
    @JsonIgnore
    private BankAccount bankAccount;
}