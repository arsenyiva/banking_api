package com.iva.banking_api.controller;

import com.iva.banking_api.model.User;
import com.iva.banking_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер для поиска пользователей.
 */
@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private UserService userService;

    /**
     * Поиск пользователей по различным параметрам.
     *
     * @param dateOfBirth дата рождения пользователя (необязательный)
     * @param phone       номер телефона пользователя (необязательный)
     * @param username    имя пользователя (необязательный)
     * @param email       адрес электронной почты пользователя (необязательный)
     * @param page        номер страницы (по умолчанию 0)
     * @param size        размер страницы (по умолчанию 10)
     * @param sortBy      поле для сортировки (по умолчанию "id")
     * @param sortOrder   порядок сортировки (по умолчанию "asc")
     * @return ResponseEntity с найденными пользователями
     */
    @Operation(summary = "Поиск пользователей",
            description = "Ищет пользователей по различным параметрам.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователи успешно найдены")
    })

    @GetMapping
    public ResponseEntity<List<User>> searchUsers(@RequestParam(required = false) String dateOfBirth,
                                                  @RequestParam(required = false) String phone,
                                                  @RequestParam(required = false) String username,
                                                  @RequestParam(required = false) String email,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "asc") String sortOrder) {

        List<User> users = userService.searchUsers(dateOfBirth, phone, username, email, page, size, sortBy, sortOrder);
        return ResponseEntity.ok(users);
    }
}