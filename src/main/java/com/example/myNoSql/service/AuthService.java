package com.example.myNoSql.service;
import com.example.myNoSql.model.User;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
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
    public Map<String, User> getUserCredentials() {
        return userCredentials;
    }
}