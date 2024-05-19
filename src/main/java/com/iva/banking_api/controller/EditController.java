package com.iva.banking_api.controller;


import com.iva.banking_api.model.User;
import com.iva.banking_api.service.UserService;
import com.iva.banking_api.util.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("{id}/edit")
public class EditController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @PatchMapping("phone")
    public ResponseEntity<String> editUserPhone(@Valid @RequestBody Map<String, Object> requestData,
                                                @PathVariable long id, HttpServletRequest request) {
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtTokenUtils.extractUsername(jwt);
        if (username == null || !userService.isUserOwnsId(username, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        String newPhone = (String) requestData.get("phone");
        if (!userService.isValidPhoneNumber(newPhone)) {
            return ResponseEntity.badRequest().body("Invalid phone number format");
        }
        if (userService.editUserPhone(id, newPhone)) {
            return ResponseEntity.ok("Phone number updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Phone number is already taken");
        }
    }

    @PatchMapping("extra_phone")
    public ResponseEntity<String> editExtraUserPhone(@Valid @RequestBody Map<String, Object> requestData,
                                                     @PathVariable long id, HttpServletRequest request) {
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtTokenUtils.extractUsername(jwt);
        if (username == null || !userService.isUserOwnsId(username, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        String newPhone = (String) requestData.get("extraPhone");
        if (!userService.isValidPhoneNumber(newPhone)) {
            return ResponseEntity.badRequest().body("Invalid phone number format");
        }
        if (userService.editExtraUserPhone(id, newPhone)) {
            return ResponseEntity.ok("Extra phone number updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Extra phone number is already taken");
        }
    }

    @DeleteMapping("phone")
    public ResponseEntity<String> deleteUserPhone(@PathVariable long id, HttpServletRequest request) {
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtTokenUtils.extractUsername(jwt);
        if (username == null || !userService.isUserOwnsId(username, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        User user = userService.getUserById(id);
        if (user.getExtraPhone() == null) {
            return ResponseEntity.badRequest().body("Cannot delete primary phone, if no extra phone exists");
        }
        user.setPhone(user.getExtraPhone());
        user.setExtraPhone(null);
        userService.updateUser(id, user);
        return ResponseEntity.ok("Primary phone deleted successfully! Now your extra phone is a primary phone");
    }

    @DeleteMapping("extra_phone")
    public ResponseEntity<String> deleteExtraUserPhone(@PathVariable long id, HttpServletRequest request) {
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtTokenUtils.extractUsername(jwt);
        if (username == null || !userService.isUserOwnsId(username, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        User user = userService.getUserById(id);
        if (user.getExtraPhone() == null) {
            return ResponseEntity.badRequest().body("Extra phone is not exist");
        }
        user.setExtraPhone(null);
        userService.updateUser(id, user);
        return ResponseEntity.ok("Extra phone deleted successfully");
    }

    @PatchMapping("email")
    public ResponseEntity<String> editEmail(@Valid @RequestBody Map<String, Object> requestData,
                                            @PathVariable long id, HttpServletRequest request) {
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtTokenUtils.extractUsername(jwt);
        if (username == null || !userService.isUserOwnsId(username, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        String newEmail = (String) requestData.get("email");
        if (!userService.isValidEmail(newEmail)) {
            return ResponseEntity.badRequest().body("Invalid email format");
        }
        if (userService.editEmail(id, newEmail)) {
            return ResponseEntity.ok("Email updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Email is already taken");
        }
    }

    @PatchMapping("extra_email")
    public ResponseEntity<String> editExtraEmail(@Valid @RequestBody Map<String, Object> requestData,
                                                 @PathVariable long id, HttpServletRequest request) {
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtTokenUtils.extractUsername(jwt);
        if (username == null || !userService.isUserOwnsId(username, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        String newEmail = (String) requestData.get("extraEmail");
        if (!userService.isValidEmail(newEmail)) {
            return ResponseEntity.badRequest().body("Invalid email format");
        }
        if (userService.editExtraEmail(id, newEmail)) {
            return ResponseEntity.ok("Email updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Email is already taken");
        }
    }

    @DeleteMapping("email")
    public ResponseEntity<String> deleteEmail(@PathVariable long id, HttpServletRequest request) {
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtTokenUtils.extractUsername(jwt);
        if (username == null || !userService.isUserOwnsId(username, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
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
        return ResponseEntity.ok("Email deleted successfully! Now your extra email is a primary email");
    }

    @DeleteMapping("extra_email")
    public ResponseEntity<String> deleteExtraEmail(@PathVariable long id, HttpServletRequest request) {
        String jwt = request.getHeader("Authorization").substring(7);
        String username = jwtTokenUtils.extractUsername(jwt);
        if (username == null || !userService.isUserOwnsId(username, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        User user = userService.getUserById(id);
        if (user.getExtraEmail() == null) {
            return ResponseEntity.badRequest().body("Extra email is not exist");
        }
        user.setExtraEmail(null);
        userService.updateUser(id, user);
        return ResponseEntity.ok("Extra email deleted successfully");
    }
}