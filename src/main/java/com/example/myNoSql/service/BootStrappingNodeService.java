package com.example.myNoSql.service;

import com.example.myNoSql.model.BootStrappingNode;
import com.example.myNoSql.model.Node;
import com.example.myNoSql.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BootStrappingNodeService {
    private final RestTemplate restTemplate;
    private Map<String ,Node> nodes;

    @Autowired
    public BootStrappingNodeService(/* Config or list of Nodes */) {
        restTemplate = new RestTemplate();
        nodes = new HashMap<>();
    }

    public Node registerNode(Node node) {
        if (!nodes.containsKey(node.getId())) {
            nodes.put(node.getId(), node);
        }
        return node;
    }

    public List<Node> getRegisteredNodes() {
        return new ArrayList<>(nodes.values());
    }

}
