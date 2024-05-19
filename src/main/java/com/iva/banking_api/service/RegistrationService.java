package com.iva.banking_api.service;

import java.time.LocalDate;

import com.iva.banking_api.model.BankAccount;
import com.iva.banking_api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired
    private UserService userService;

    @Autowired
    private BankAccountService bankAccountService;

    private final PasswordEncoder passwordEncoder;

    public RegistrationService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public boolean registerNewUser(String username, String password,
                                   LocalDate dateOfBirth, String phone, String extraPhone,
                                   String email, Double initialBalance) {
        if (!userService.isPhoneAvailable(phone) ||
                !userService.isEmailAvailable(email)) {
            return false;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setDateOfBirth(dateOfBirth);
        newUser.setPhone(phone);
        newUser.setExtraPhone(extraPhone);
        newUser.setEmail(email);

        BankAccount newBankAccount = new BankAccount();
        newBankAccount.setBalance(initialBalance);
        newBankAccount.setOwner(newUser);

        userService.createUser(newUser);
        bankAccountService.createAccount(newBankAccount);
        return true;
    }
}

