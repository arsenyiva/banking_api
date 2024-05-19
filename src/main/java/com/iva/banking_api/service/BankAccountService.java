package com.iva.banking_api.service;

import com.iva.banking_api.model.BankAccount;
import com.iva.banking_api.model.User;
import com.iva.banking_api.repository.BankAccountRepository;
import com.iva.banking_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    public BankAccount createAccount(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }
}
