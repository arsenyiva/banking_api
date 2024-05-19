package com.iva.banking_api.repository;

import com.iva.banking_api.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для выполнения операций с банковскими счетами в базе данных.
 */
@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    BankAccount findByOwnerId(Long receiverId);
}
