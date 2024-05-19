package com.iva.banking_api.service;

import com.iva.banking_api.model.BankAccount;
import com.iva.banking_api.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для управления банковскими счетами.
 */
@Service
public class BankAccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    public void createAccount(BankAccount bankAccount) {
        bankAccountRepository.save(bankAccount);
    }

    @Transactional(readOnly = true)
    public BankAccount findByOwner(Long receiverId) {
        return bankAccountRepository.findByOwnerId(receiverId);
    }

    @Transactional
    public void save(BankAccount bankAccount) {
        bankAccountRepository.save(bankAccount);
    }
}
