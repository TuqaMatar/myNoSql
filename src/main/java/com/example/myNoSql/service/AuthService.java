package com.example.myNoSql.service;

import com.example.myNoSql.AdminProperties;
import com.example.myNoSql.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class AuthService {
    @Autowired
    private AdminProperties adminProperties;

    private Map<String, User> userCredentials;

    public AuthService() {
        this.userCredentials = new HashMap<>();
    }

    public void registerUser(User user) {
        userCredentials.put(user.getUsername(), user);
    }

    public User authenticate(String username, String password) {
        User user = userCredentials.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean authenticateAdmin(String adminUsername, String adminPassword) {
        if (!adminUsername.equals(adminProperties.getUsername()) )
            return false;

        if (!adminPassword.equals(adminProperties.getPassword()))
            return false;

        return  true;
    }

    public Map<String, User> getUserCredentials() {
        return userCredentials;
    }
}