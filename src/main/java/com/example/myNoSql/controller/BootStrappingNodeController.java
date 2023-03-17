package com.example.myNoSql.controller;

import com.example.myNoSql.model.Node;
import com.example.myNoSql.model.User;
import com.example.myNoSql.service.BootStrappingNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/bootstrapping-node")
public class BootStrappingNodeController {
    private final BootStrappingNodeService bootstrappingNodeService;

    @Autowired
    public BootStrappingNodeController(BootStrappingNodeService bootstrappingNodeService) {
        this.bootstrappingNodeService = bootstrappingNodeService;
    }


    @GetMapping("/ping-nodes")
    public ResponseEntity<Map<String, Boolean>> pingNodes() {
        Map<String, Boolean> pingResults = new HashMap<>();

        for (String nodeUrl : bootstrappingNodeService.getNodeUrls()) {
            boolean pingResult = bootstrappingNodeService.pingNode(nodeUrl);
            pingResults.put(nodeUrl, pingResult);
        }

        return ResponseEntity.ok(pingResults);
    }

//    @PostMapping("/register")
//    public ResponseEntity<User> registerUser(@RequestBody UserRegistrationRequest request) {
//        // Use bootstrappingNodeService to register a new user and return the user object
//    }
//
//    @GetMapping("/assigned-node/{userId}")
//    public ResponseEntity<Node> getAssignedNode(@PathVariable String userId) {
//        // Use bootstrappingNodeService to get the assigned node for the given user ID
//    }

    // Other API endpoints for bootstrapping node management
}
