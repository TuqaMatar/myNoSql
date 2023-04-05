package com.example.myNoSql.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Node {
    private String name ;
    private String ipAddress;
    private int port;
    private List<User> users;

    public Node( String ipAddress) {
        this.ipAddress = ipAddress;
        this.users = new ArrayList<>();
    }

    public Node() {
        this.users = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
       // userNamePasswordMap.put(user.getUsername(),user.getPassword());
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
