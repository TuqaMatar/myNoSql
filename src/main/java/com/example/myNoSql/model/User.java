package com.example.myNoSql.model;


public class User {
    private String id;
    private String username;
    private String password;
    private String assignedNodeId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAssignedNodeId() {
        return assignedNodeId;
    }

    public void setAssignedNodeId(String assignedNodeId) {
        this.assignedNodeId = assignedNodeId;
    }


    // Constructors, getters, and setters
}
