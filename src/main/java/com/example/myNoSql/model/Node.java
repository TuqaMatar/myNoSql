package com.example.myNoSql.model;

import java.util.HashMap;
import java.util.Map;

public class Node {
    private String id;
    private String ipAddress;
    private Map<String, Database> databases;

    public Node(String id, String ipAddress) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.databases = new HashMap<>();
    }

    // Getters, setters, and other methods
}
