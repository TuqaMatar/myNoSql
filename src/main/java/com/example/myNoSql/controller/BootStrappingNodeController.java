package com.example.myNoSql.controller;

import com.example.myNoSql.BootStrapProperties;
import com.example.myNoSql.model.Node;
import com.example.myNoSql.model.User;
import com.example.myNoSql.service.AuthService;
import com.example.myNoSql.service.BootStrappingNodeService;
import com.example.myNoSql.service.FileStorageService;
import com.example.myNoSql.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/bootstrap")

public class BootStrappingNodeController {
    @Autowired
    private BootStrapProperties bootstrapProperties;
    @Autowired
    UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private BootStrappingNodeService bootStrappingNodeService;
    @Autowired
    private FileStorageService fileStorageService;
    List<User> users;


    @PostConstruct
    public void init() {
        users = fileStorageService.loadUsersFromFile();
        System.out.println(users);
        for(User user : users)
        {
            System.out.println(user.getUsername());
            userService.addUser(user);
            authService.registerUser(user);
        }
    }


    private AtomicInteger nodeCounter = new AtomicInteger(0);

    @PostMapping("/registerUser")
    public ResponseEntity<User> registerUser(@RequestBody User newUser) {
        List<Node> nodes = bootstrapProperties.getNodes();

        // Check if the username is already in use
        for (User user : users) {
            if (user.getUsername().equals(newUser.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(null);
            }
        }

        // Assign the new user to a node using round-robin load balancing algorithm
        Node assignedNode = nodes.get(nodeCounter.getAndIncrement() % nodes.size());
        newUser.setId(UUID.randomUUID().toString());
        newUser.setPort(assignedNode.getPort());
        assignedNode.addUser(newUser);

        users.add(newUser);
        userService.addUser(newUser);
        authService.registerUser(newUser);
        bootStrappingNodeService.registerUserToNode(newUser , assignedNode);
        fileStorageService.saveUserToFile(newUser);

        return ResponseEntity.ok(newUser);
    }
    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> getUsers ()
    {
        List<User> users = new ArrayList<>(userService.getUsers().values());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/nodes")
    public ResponseEntity<List<Node>> getNodes ()
    {
        return ResponseEntity.ok(bootstrapProperties.getNodes());
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<String> getUserNode(@PathVariable String username) {
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

        String assignedNode = foundUser.getAssignedNode();
        return ResponseEntity.ok(assignedNode);
    }
}
