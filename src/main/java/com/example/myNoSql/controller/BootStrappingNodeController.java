package com.example.myNoSql.controller;

import com.example.myNoSql.BootStrapProperties;
import com.example.myNoSql.model.Node;
import com.example.myNoSql.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class BootStrappingNodeController {

    @Autowired
    private BootStrapProperties bootstrapProperties;

    private AtomicInteger nodeCounter = new AtomicInteger(0);

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User newUser) {
        List<User> users = bootstrapProperties.getUsers();
        List<Node> nodes = bootstrapProperties.getNodes();
        // Check if the username is already in use
        for (User user : users) {
            if (user.getUsername().equals(newUser.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(null);
            }
        }
        // Assign the new user to a node using round-robin load balancing
        Node assignedNode = nodes.get(nodeCounter.getAndIncrement() % nodes.size());
        // assignedNode.addUser(newUser);

        newUser.setAssignedNode(assignedNode);
        newUser.setId(UUID.randomUUID().toString());

        users.add(newUser);
        return ResponseEntity.ok(newUser);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<Node> getUserNode(@PathVariable String username) {
        List<User> users = bootstrapProperties.getUsers();
        // Find the user by username
        User foundUser = null;
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                foundUser = user;
                break;
            }
        }

        if (foundUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Node assignedNode = foundUser.getAssignedNode();
        return ResponseEntity.ok(assignedNode);
    }
}
