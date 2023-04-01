package com.example.myNoSql.service;

import com.example.myNoSql.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    Map<String , User> users;

    public UserService()
    {
        this.users = new HashMap<>();
    }
    public void addUser(User user)
    {
        users.put(user.getUsername() , user);
    }

    public Map<String, User> getUsers() {
        return users;
    }
}
