package com.example.myNoSql.model;


public class User {
    private String id;
    private String username;
    private String password;
    private String assignedNode;
    private Integer port ;
    public User() {
    }

    public User(String id, String username, String password, String assignedNode) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.assignedNode = assignedNode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getAssignedNode() {
        return assignedNode;
    }

    public void setAssignedNode(String assignedNode) {
        this.assignedNode = assignedNode;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}