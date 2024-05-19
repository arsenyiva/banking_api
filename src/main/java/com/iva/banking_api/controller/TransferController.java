package com.iva.banking_api.controller;

import com.iva.banking_api.model.BankAccount;
import com.iva.banking_api.model.pojo.TransferRequest;
import com.iva.banking_api.service.BankAccountService;
import com.iva.banking_api.service.TransferService;
import com.iva.banking_api.service.UserService;
import com.iva.banking_api.util.JwtTokenUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.log4j.Logger;

/**
 * Контроллер для выполнения денежных переводов.
 */
@RestController
@RequestMapping("transfer/{id}")
public class TransferController {
    private static final Logger log = Logger.getLogger(TransferController.class);


    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private TransferService transferService;

    /**
     * Выполняет денежный перевод с одного банковского счета на другой.
     *
     * @param id              идентификатор отправителя перевода
     * @param request         объект HttpServletRequest для проверки прав доступа
     * @param transferRequest объект, содержащий информацию о переводе (идентификатор получателя и сумма)
     * @return ResponseEntity с сообщением о результате операции
     */
    @Operation(summary = "Выполнить денежный перевод",
            description = "Выполняет денежный перевод с одного банковского счета на другой.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Перевод успешно выполнен"),
            @ApiResponse(responseCode = "400", description = "Недостаточно средств на счете отправителя"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Счет отправителя или получателя не найден")
    })
    @PostMapping
    public ResponseEntity<?> transferMoney(@PathVariable long id, HttpServletRequest request, @RequestBody TransferRequest transferRequest) {
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtTokenUtils.extractUsername(jwt);
        if (username == null || !userService.isUserOwnsId(username, id)) {
            log.error("Access denied");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return transferService.transferMoney(id, transferRequest);
    }

}