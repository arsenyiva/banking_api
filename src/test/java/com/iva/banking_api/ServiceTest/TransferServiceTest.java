package com.iva.banking_api.ServiceTest;

import com.iva.banking_api.model.BankAccount;
import com.iva.banking_api.model.pojo.TransferRequest;
import com.iva.banking_api.service.BankAccountService;
import com.iva.banking_api.service.TransferService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Тестовый класс для проверки функционала класса TransferService.
 */
public class TransferServiceTest {

    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private TransferService transferService;

    public TransferServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTransferMoney_SuccessfulTransfer() {
        // Arrange
        long senderId = 1;
        long receiverId = 2;
        double amount = 100.0;
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setReceiverId(receiverId);
        transferRequest.setAmount(amount);

        BankAccount senderAccount = new BankAccount();
        senderAccount.setId(senderId);
        senderAccount.setBalance(200.0);

        BankAccount receiverAccount = new BankAccount();
        receiverAccount.setId(receiverId);
        receiverAccount.setBalance(50.0);

        when(bankAccountService.findByOwner(senderId)).thenReturn(senderAccount);
        when(bankAccountService.findByOwner(receiverId)).thenReturn(receiverAccount);

        // Act
        ResponseEntity<?> responseEntity = transferService.transferMoney(senderId, transferRequest);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Transfer successful", responseEntity.getBody());

        assertEquals(100.0, senderAccount.getBalance());
        assertEquals(150.0, receiverAccount.getBalance());

        verify(bankAccountService, times(1)).save(senderAccount);
        verify(bankAccountService, times(1)).save(receiverAccount);
    }

}