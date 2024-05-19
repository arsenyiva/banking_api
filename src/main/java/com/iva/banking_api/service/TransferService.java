package com.iva.banking_api.service;

import com.iva.banking_api.model.BankAccount;
import com.iva.banking_api.model.pojo.TransferRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Сервис для выполнения операций по переводу денег между банковскими счетами.
 */
@Service
public class TransferService {


    @Autowired
    private BankAccountService bankAccountService;

    /**
     * Выполняет перевод денег с одного счета на другой.
     *
     * @param senderId         идентификатор отправителя
     * @param transferRequest  информация о переводе
     * @return ResponseEntity с результатом операции
     */
    public ResponseEntity<?> transferMoney(long senderId, TransferRequest transferRequest) {


        BankAccount senderAccount = bankAccountService.findByOwner(senderId);
        BankAccount receiverAccount = bankAccountService.findByOwner(transferRequest.getReceiverId());

        return transfer(senderAccount, receiverAccount, transferRequest.getAmount());
    }
    /**
     * Приватный метод для фактического перевода денег между счетами.
     *
     * @param senderAccount    банковский счет отправителя
     * @param receiverAccount  банковский счет получателя
     * @param amount           сумма перевода
     * @return ResponseEntity с результатом операции
     */
    private ResponseEntity<?> transfer(BankAccount senderAccount, BankAccount receiverAccount, double amount) {
        if (senderAccount == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sender account not found");
        }

        if (receiverAccount == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Receiver account not found");
        }

        if (senderAccount.getBalance() < amount) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient funds");
        }

        synchronized (this) {
            senderAccount.setBalance(senderAccount.getBalance() - amount);
            receiverAccount.setBalance(receiverAccount.getBalance() + amount);
            bankAccountService.save(senderAccount);
            bankAccountService.save(receiverAccount);
        }

        return ResponseEntity.ok("Transfer successful");
    }
}