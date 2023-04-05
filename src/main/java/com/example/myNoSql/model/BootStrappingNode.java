package com.example.myNoSql.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class BootStrappingNode {
    private List<Node> nodes;
    private Map<String, User> users;
    private AtomicInteger nodeCounter = new AtomicInteger(0);

    public BootStrappingNode(List<String> nodeIpAddresses) {
        nodes = new ArrayList<>();
        for (int i = 0; i < nodeIpAddresses.size(); i++) {
            Node node = new Node( nodeIpAddresses.get(i));
            nodes.add(node);
        }
        this.users = new HashMap<>();
    }

  }