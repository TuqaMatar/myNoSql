package com.example.myNoSql.service;
import com.example.myNoSql.model.Node;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NodeService {

    // Define methods to handle Node operations here, e.g., registerNode, getNode, etc.

    private final Map<String, Node> nodes;

    public NodeService() {
        nodes = new HashMap<>();
        // You can add the nodes to the map in the constructor or through another method
    }

    public Node getNode(String nodeId) {
        return nodes.get(nodeId);
    }


}
