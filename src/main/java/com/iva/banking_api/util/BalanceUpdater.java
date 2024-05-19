package com.iva.banking_api.util;

import com.iva.banking_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Компонент для обновления балансов пользователей по расписанию.
 */
@Component
public class BalanceUpdater {

    @Autowired
    private UserService userService;

    @Scheduled(fixedRate = 60000)
    public void updateBalances() {
        userService.updateUserBalances();
    }
}