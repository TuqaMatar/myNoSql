package com.example.myNoSql.controller;
import com.example.myNoSql.AdminProperties;
import com.example.myNoSql.model.User;
import com.example.myNoSql.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminProperties adminProperties;

    @Autowired
    private AuthService authService;

    public AdminController() {
    }

    @PostMapping("/login")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        System.out.println("here");
        if (authService.authenticateAdmin(user.getUsername(), user.getPassword()))
            return ResponseEntity.ok("authenticated");

        return ResponseEntity.ok("unauthenticated");
    }

}