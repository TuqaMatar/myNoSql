package com.example.myNoSql.controller;
import com.example.myNoSql.BootStrapProperties;
import com.example.myNoSql.model.BootStrappingNode;
import com.example.myNoSql.model.Node;
import com.example.myNoSql.model.User;
import com.example.myNoSql.service.AuthService;
import com.example.myNoSql.service.FileStorageService;
import com.example.myNoSql.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;

@RestController
@RequestMapping("/api/nodes")
public class NodeController {
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

    @Autowired
    private BootStrappingNode bootstrapNode;

    @Autowired
    UserService userService;

    @Autowired
    private BootStrapProperties bootstrapProperties;
    @Autowired
    private AuthService authService;

    @Value("${CONTAINER_NAME:unknown}")
    private String containerName;

    public NodeController() {
    }

    @PostMapping("/login")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        System.out.println("here");
        if(authService.authenticate(user.getUsername(), user.getPassword()) == null)
            return ResponseEntity.ok("unauthenticated");
            User loggedUser = userService.getUsers().get(user.getUsername());
//        if(loggedUser.getAssignedNode() !=containerName)
//            return ResponseEntity.ok("unauthorized");

        return ResponseEntity.ok("authenticated");
    }

    @PostMapping("/addUser")
    public  void addUser (@RequestBody User user)
    {

        fileStorageService.saveUserToFile(user);
        authService.registerUser(user);
    }

    @GetMapping("/authenticate")
    public User authenticate(@RequestParam String username, @RequestParam String password) {
        return authService.authenticate(username, password);
    }

    @GetMapping("/getNodeUsers")
    public ResponseEntity<Map<String, User>> getUsers ()
    {
        authService.getUserCredentials();

        return ResponseEntity.ok( authService.getUserCredentials());
    }

}