package com.example.myNoSql.controller;

import com.example.myNoSql.model.BootStrappingNode;
import com.example.myNoSql.model.Node;
import com.example.myNoSql.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
//    @Autowired
//    private BootStrappingNode bootStrappingNode;
//
//    @GetMapping("/ping")
//    public ResponseEntity<String> ping() {
//        return ResponseEntity.ok("pong");
//    }
//    @PostMapping("/signup")
//    public ResponseEntity<String> signUp(@ModelAttribute User user) {
//
//        // Assign the user to a node
//        Node assignedNode = bootStrappingNode.assignUserToNode(user);
//
//        // Return the assigned node's address
//        return new ResponseEntity<>(assignedNode.getIpAddress(), HttpStatus.CREATED);
//    }
}