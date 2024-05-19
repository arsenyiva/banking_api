package com.iva.banking_api.controller;


import com.iva.banking_api.model.User;
import com.iva.banking_api.service.UserService;
import com.iva.banking_api.util.JwtTokenUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Контроллер для редактирования и удаления данных пользователей.
 */
@RestController
@RequestMapping("/edit/{id}")
public class EditController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    private static final org.apache.log4j.Logger log = Logger.getLogger(AuthController.class);

    /**
     * Метод для редактирования основного телефонного номера пользователя.
     *
     * @param requestData Запрос с новыми данными пользователя
     * @param id          Идентификатор пользователя
     * @param request     HTTP-запрос
     * @return Ответ с результатом операции
     */
    @Operation(summary = "Изменение номера телефона пользователя", description = "Изменение номера телефона пользователя по их ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Номер телефона успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос, недопустимый формат номера телефона или номер телефона уже занят"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PatchMapping("phone")
    public ResponseEntity<String> editUserPhone(@Valid @RequestBody Map<String, Object> requestData,
                                                @PathVariable long id, HttpServletRequest request) {
        ResponseEntity<String> accessDeniedResponse = checkAccessRights(request, id);
        if (accessDeniedResponse != null) {
            return accessDeniedResponse;
        }
        String newPhone = (String) requestData.get("phone");
        if (!userService.isValidPhoneNumber(newPhone)) {
            log.error("Invalid phone number format");
            return ResponseEntity.badRequest().body("Invalid phone number format");
        }
        if (userService.editUserPhone(id, newPhone)) {
            log.debug("Phone number updated successfully");
            return ResponseEntity.ok("Phone number updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Phone number is already taken");
        }
    }

    /**
     * Метод для редактирования дополнительного номера телефона пользователя.
     *
     * @param requestData данные запроса, содержащие новый дополнительный номер телефона
     * @param id          идентификатор пользователя
     * @param request     объект запроса HttpServletRequest для проверки прав доступа
     * @return ResponseEntity с сообщением о результате операции
     */
    @Operation(summary = "Редактировать дополнительный номер телефона пользователя",
            description = "Редактировать дополнительный номер телефона пользователя по его идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Дополнительный номер телефона успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Неверный формат номера телефона или номер телефона уже занят"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PatchMapping("extra_phone")
    public ResponseEntity<String> editExtraUserPhone(@Valid @RequestBody Map<String, Object> requestData,
                                                     @PathVariable long id, HttpServletRequest request) {
        ResponseEntity<String> accessDeniedResponse = checkAccessRights(request, id);
        if (accessDeniedResponse != null) {
            return accessDeniedResponse;
        }
        String newPhone = (String) requestData.get("extraPhone");
        if (!userService.isValidPhoneNumber(newPhone)) {
            log.error("Invalid phone number format");
            return ResponseEntity.badRequest().body("Invalid phone number format");
        }
        if (userService.editExtraUserPhone(id, newPhone)) {
            log.debug("Extra phone number updated successfully");
            return ResponseEntity.ok("Extra phone number updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Extra phone number is already taken");
        }
    }

    /**
     * Метод для удаления основного номера телефона пользователя.
     *
     * @param id      идентификатор пользователя
     * @param request объект HttpServletRequest для проверки прав доступа
     * @return ResponseEntity с сообщением о результате операции
     */
    @Operation(summary = "Удаление номера телефона пользователя", description = "Удаление номера телефона пользователя по его идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Основной номер телефона успешно удален"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос, нельзя удалить основной номер телефона, если отсутствует дополнительный номер"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("phone")
    public ResponseEntity<String> deleteUserPhone(@PathVariable long id, HttpServletRequest request) {
        ResponseEntity<String> accessDeniedResponse = checkAccessRights(request, id);
        if (accessDeniedResponse != null) {
            return accessDeniedResponse;
        }
        User user = userService.getUserById(id);
        if (user.getExtraPhone() == null) {
            return ResponseEntity.badRequest().body("Cannot delete primary phone, if no extra phone exists");
        }
        user.setPhone(user.getExtraPhone());
        user.setExtraPhone(null);
        userService.updateUser(id, user);
        log.debug("Primary phone deleted successfully!");
        return ResponseEntity.ok("Primary phone deleted successfully! Now your extra phone is a primary phone");
    }

    /**
     * Метод для удаления дополнительного номера телефона пользователя.
     *
     * @param id      идентификатор пользователя
     * @param request объект HttpServletRequest для проверки прав доступа
     * @return ResponseEntity с сообщением о результате операции
     */
    @Operation(summary = "Удалить дополнительный номер телефона пользователя",
            description = "Удалить дополнительный номер телефона пользователя по его идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Дополнительный номер телефона успешно удален"),
            @ApiResponse(responseCode = "400", description = "Дополнительный номер телефона не существует"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("extra_phone")
    public ResponseEntity<String> deleteExtraUserPhone(@PathVariable long id, HttpServletRequest request) {
        ResponseEntity<String> accessDeniedResponse = checkAccessRights(request, id);
        if (accessDeniedResponse != null) {
            return accessDeniedResponse;
        }
        User user = userService.getUserById(id);
        if (user.getExtraPhone() == null) {
            return ResponseEntity.badRequest().body("Extra phone is not exist");
        }
        user.setExtraPhone(null);
        userService.updateUser(id, user);
        log.debug("Extra phone deleted successfully");
        return ResponseEntity.ok("Extra phone deleted successfully");
    }

    /**
     * Метод для изменения адреса электронной почты пользователя.
     *
     * @param requestData данные запроса, содержащие новый адрес электронной почты
     * @param id          идентификатор пользователя
     * @param request     объект HttpServletRequest для проверки прав доступа
     * @return ResponseEntity с сообщением о результате операции
     */
    @Operation(summary = "Изменить адрес электронной почты пользователя",
            description = "Изменить адрес электронной почты пользователя по его идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Адрес электронной почты успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Неверный формат электронной почты или электронная почта уже занята"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PatchMapping("email")
    public ResponseEntity<String> editEmail(@Valid @RequestBody Map<String, Object> requestData,
                                            @PathVariable long id, HttpServletRequest request) {
        ResponseEntity<String> accessDeniedResponse = checkAccessRights(request, id);
        if (accessDeniedResponse != null) {
            return accessDeniedResponse;
        }
        String newEmail = (String) requestData.get("email");
        if (!userService.isValidEmail(newEmail)) {
            return ResponseEntity.badRequest().body("Invalid email format");
        }
        if (userService.editEmail(id, newEmail)) {
            log.debug("Email updated successfully");
            return ResponseEntity.ok("Email updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Email is already taken");
        }
    }

    /**
     * Метод для изменения доп адреса электронной почты пользователя.
     *
     * @param requestData данные запроса, содержащие новый дополнительный адрес электронной почты
     * @param id          идентификатор пользователя
     * @param request     объект HttpServletRequest для проверки прав доступа
     * @return ResponseEntity с сообщением о результате операции
     */
    @Operation(summary = "Изменить дополнительный адрес электронной почты пользователя",
            description = "Изменить дополнительный адрес электронной почты пользователя по его идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Дополнительный адрес электронной почты успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Неверный формат электронной почты или электронная почта уже занята"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PatchMapping("extra_email")
    public ResponseEntity<String> editExtraEmail(@Valid @RequestBody Map<String, Object> requestData,
                                                 @PathVariable long id, HttpServletRequest request) {
        ResponseEntity<String> accessDeniedResponse = checkAccessRights(request, id);
        if (accessDeniedResponse != null) {
            return accessDeniedResponse;
        }
        String newEmail = (String) requestData.get("extraEmail");
        if (!userService.isValidEmail(newEmail)) {
            return ResponseEntity.badRequest().body("Invalid email format");
        }
        if (userService.editExtraEmail(id, newEmail)) {
            log.debug("Email updated successfully");
            return ResponseEntity.ok("Email updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Email is already taken");
        }
    }

    /**
     * Метод для удаления доп адреса электронной почты пользователя.
     *
     * @param id      идентификатор пользователя
     * @param request объект HttpServletRequest для проверки прав доступа
     * @return ResponseEntity с сообщением о результате операции
     */
    @Operation(summary = "Удалить дополнительный адрес электронной почты",
            description = "Удаляет дополнительный адрес электронной почты пользователя и делает основным адресом тот, который был дополнительным.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Дополнительный адрес электронной почты успешно удален"),
            @ApiResponse(responseCode = "400", description = "Невозможно удалить основной адрес электронной почты, если дополнительного адреса не существует"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("email")
    public ResponseEntity<String> deleteEmail(@PathVariable long id, HttpServletRequest request) {
        ResponseEntity<String> accessDeniedResponse = checkAccessRights(request, id);
        if (accessDeniedResponse != null) {
            return accessDeniedResponse;
        }
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (user.getExtraEmail() == null) {
            return ResponseEntity.badRequest().body("Cannot delete primary email, if no extra email exists");
        }
        user.setEmail(user.getExtraEmail());
        user.setExtraEmail(null);
        userService.updateUser(id, user);
        log.debug("Email deleted successfully");
        return ResponseEntity.ok("Email deleted successfully! Now your extra email is a primary email");
    }

    /**
     * Метод для удаления основного адреса электронной почты пользователя.
     *
     * @param id      идентификатор пользователя
     * @param request объект HttpServletRequest для проверки прав доступа
     * @return ResponseEntity с сообщением о результате операции
     */
    @Operation(summary = "Удалить основной адрес электронной почты пользователя",
            description = "Удалить основной адрес электронной почты пользователя по его идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Основной адрес электронной почты успешно удален"),
            @ApiResponse(responseCode = "400", description = "Невозможно удалить основной адрес электронной почты, если дополнительного адреса не существует"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping("extra_email")
    public ResponseEntity<String> deleteExtraEmail(@PathVariable long id, HttpServletRequest request) {
        ResponseEntity<String> accessDeniedResponse = checkAccessRights(request, id);
        if (accessDeniedResponse != null) {
            return accessDeniedResponse;
        }
        User user = userService.getUserById(id);
        if (user.getExtraEmail() == null) {
            return ResponseEntity.badRequest().body("Extra email is not exist");
        }
        user.setExtraEmail(null);
        userService.updateUser(id, user);
        log.debug("Extra email deleted successfully");

        return ResponseEntity.ok("Extra email deleted successfully");
    }

    /**
     * Метод для проверки права доступа пользователя к ресурсу.
     *
     * @param request объект HttpServletRequest для извлечения заголовка авторизации
     * @param id      идентификатор пользователя
     * @return ResponseEntity с сообщением об ошибке доступа, если доступ запрещен, иначе null
     */
    private ResponseEntity<String> checkAccessRights(HttpServletRequest request, long id) {
        String username = jwtTokenUtils.extractUsername(request.getHeader("Authorization").substring(7));
        if (username == null || !userService.isUserOwnsId(username, id)) {
            log.error("Access denied");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return null;
    }
}