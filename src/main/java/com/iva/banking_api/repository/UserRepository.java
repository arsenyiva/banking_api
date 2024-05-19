package com.iva.banking_api.repository;

import com.iva.banking_api.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByUsername(String username);

    User findByEmail(String email);

    User findByPhone(String phone);
    User findByExtraPhone(String phone);

    void deleteByIdAndExtraPhoneNotNull(Long id);

    void deleteByIdAndPhoneNotNull(Long id);

    User findByExtraEmail(String email);
}
