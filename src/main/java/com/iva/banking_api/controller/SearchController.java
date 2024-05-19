package com.iva.banking_api.controller;

import com.iva.banking_api.model.User;
import com.iva.banking_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> searchUsers(@RequestParam(required = false) String dateOfBirth,
                                                  @RequestParam(required = false) String phone,
                                                  @RequestParam(required = false) String username,
                                                  @RequestParam(required = false) String email,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "asc") String sortOrder) {

        List<User> users = userService.searchUsers(dateOfBirth, phone, username, email, page,  size, sortBy, sortOrder);
        return ResponseEntity.ok(users);
    }
}