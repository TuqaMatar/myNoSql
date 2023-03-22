package com.example.myNoSql;

import com.example.myNoSql.model.Node;
import com.example.myNoSql.model.User;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "bootstrap")
public class BootStrapProperties {
    private List<User> users;
    private List<Node> nodes;
    private List<String> nodeIPs;

    // Getters and setters


    public List<String> getNodeIPs() {
        return nodeIPs;
    }

    public void setNodeIPs(List<String> nodeIPs) {
        this.nodeIPs = nodeIPs;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }
}