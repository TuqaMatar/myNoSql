package com.example.myNoSql.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BootStrappingNode {
    private List<Node> nodes;
    private Map<String, User> users;
    private List<Node> nodeList;
    private int nextNodeIndex;

    public BootStrappingNode(List<String> nodeIpAddresses) {
        nodes = new ArrayList<>();
        for (int i = 0; i < nodeIpAddresses.size(); i++) {
            Node node = new Node( nodeIpAddresses.get(i));
            nodes.add(node);
        }
        this.users = new HashMap<>();
        this.nodeList = new ArrayList<>();
        this.nextNodeIndex = 0;
    }
    public Node assignNodeToUser(String userId) {
        int nodeIndex = Math.abs(userId.hashCode()) % nodes.size();
        return nodes.get(nodeIndex);
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public void setUsers(Map<String, User> users) {
        this.users = users;
    }
}