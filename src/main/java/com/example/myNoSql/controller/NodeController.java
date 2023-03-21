package com.example.myNoSql.controller;
import com.example.myNoSql.model.BootStrappingNode;
import com.example.myNoSql.model.Node;
import com.example.myNoSql.model.User;
import com.example.myNoSql.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nodes")
public class NodeController {

    @Autowired
    private BootStrappingNode bootstrapNode;

    @Autowired
    private AuthService authService;

    public NodeController() {
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        Node assignedNode = bootstrapNode.assignNodeToUser(user.getId());
        user.setAssignedNode(assignedNode);
        authService.registerUser(user);
        return user;
    }

    @GetMapping("/authenticate")
    public User authenticate(@RequestParam String username, @RequestParam String password) {
        return authService.authenticate(username, password);
    }
}