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
import java.util.List;

@Service
public class BootStrappingNodeService {
    private final RestTemplate restTemplate;
    private List<String> nodeUrls;

    @Autowired
    public BootStrappingNodeService(/* Config or list of Nodes */) {
        // Initialize bootstrappingNode with the provided configuration
        // or list of nodes

        restTemplate = new RestTemplate();
        nodeUrls = new ArrayList<>();
        nodeUrls.add("http://node-1:8080");
        nodeUrls.add("http://node-2:8080");
    }


    public boolean pingNode(String nodeUrl) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(nodeUrl + "/node/ping", String.class);
            return response.getStatusCode() == HttpStatus.OK && "pong".equals(response.getBody());
        } catch (RestClientException e) {
            return false;
        }
    }

    public List<String> getNodeUrls() {
        return nodeUrls;
    }

    //    public User registerUser(String username, String password) {
//        // Create a new user, assign a node, and store it in the bootstrappingNode's user map
//    }
//
//    public Node getAssignedNode(String userId) {
//        // Retrieve assigned node for the given user
//    }

    // Other methods for managing the bootstrapping node
}
