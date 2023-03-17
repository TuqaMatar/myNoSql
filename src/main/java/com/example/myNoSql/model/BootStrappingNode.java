package com.example.myNoSql.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BootStrappingNode {
    private String id;
    private String ipAddress;
    private List<Node> nodes;
    private Map<String, User> users;

    public BootStrappingNode(String id, String ipAddress, List<Node> nodes) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.nodes = nodes;
        this.users = new HashMap<>();
    }

    public void addNode(Node node) {
        nodes.add(node);
    }
    // Getters, setters, and other methods
}
