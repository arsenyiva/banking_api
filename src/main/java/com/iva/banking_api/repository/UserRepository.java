package com.iva.banking_api.repository;

import com.iva.banking_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для выполнения операций с пользователями в базе данных.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByUsername(String username);

    User findByEmail(String email);

    User findByPhone(String phone);

    User findByExtraPhone(String phone);

    User findByExtraEmail(String email);
}
