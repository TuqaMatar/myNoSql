package com.example.myNoSql.controller;

import com.example.myNoSql.AdminProperties;
import com.example.myNoSql.BootStrapProperties;
import com.example.myNoSql.model.BootStrappingNode;
import com.example.myNoSql.model.User;
import com.example.myNoSql.service.AuthService;
import com.example.myNoSql.service.FileStorageService;
import com.example.myNoSql.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
@RequestMapping("/api/nodes")
public class NodeController {

    List<User> users;
    @Autowired
    UserService userService;
    @Autowired
    private BootStrapProperties bootstrapProperties;
    @Autowired
    private AdminProperties adminProperties;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private BootStrappingNode bootstrapNode;
    @Autowired
    private AuthService authService;

    public NodeController() {
    }

    @PostConstruct
    public void init() {
        users = fileStorageService.loadUsersFromFile();
        System.out.println(users);
        for (User user : users) {
            System.out.println(user.getUsername());
            userService.addUser(user);
            authService.registerUser(user);
        }


        // Check if the admin user already exists in the persisted users list
        boolean adminExists = false;
        for (User user : users) {
            if (user.getUsername().equals(adminProperties.getUsername())) {
                adminExists = true;
                break;
            }
        }

        // If the admin user does not exist, create and save it
        if (!adminExists) {
            User adminUser = new User();
            adminUser.setUsername(adminProperties.getUsername());
            adminUser.setPassword(adminProperties.getPassword());
            // Consider hashing the password before storing it
            adminUser.setId(UUID.randomUUID().toString());
            adminUser.setAssignedNode("boot-strap-node");
            adminUser.setPort(8081);
            // Save the admin user to a file
            fileStorageService.saveUserToFile(adminUser);

            // Add the admin user to the list of users
            users.add(adminUser);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        System.out.println("here");
        if (authService.authenticate(user.getUsername(), user.getPassword()) == null)
            return ResponseEntity.ok("unauthenticated");
        User loggedUser = userService.getUsers().get(user.getUsername());
//        if(loggedUser.getAssignedNode() !=containerName)
//            return ResponseEntity.ok("unauthorized");

        return ResponseEntity.ok("authenticated");
    }

    @PostMapping("/addUser")
    public void addUser(@RequestBody User user) {

        fileStorageService.saveUserToFile(user);
        authService.registerUser(user);
    }

    @GetMapping("/authenticate")
    public User authenticate(@RequestParam String username, @RequestParam String password) {
        return authService.authenticate(username, password);
    }

    @GetMapping("/getNodeUsers")
    public ResponseEntity<Map<String, User>> getUsers() {
        authService.getUserCredentials();

        return ResponseEntity.ok(authService.getUserCredentials());
    }

}