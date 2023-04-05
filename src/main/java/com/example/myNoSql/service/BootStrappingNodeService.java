package com.example.myNoSql.service;
import com.example.myNoSql.Constants;
import com.example.myNoSql.model.Node;
import com.example.myNoSql.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.ConstantCallSite;


@Service
public class BootStrappingNodeService {
    private final RestTemplate restTemplate;

    @Autowired
    public BootStrappingNodeService(/* Config or list of Nodes */) {
        restTemplate = new RestTemplate();
    }

    public void registerUserToNode (User user , Node node){
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://" + node.getName() + ":" + Constants.CONTAINER_PORT + "/api/nodes/addUser";
            System.out.println("Registering to " + url);
            restTemplate.postForEntity(url, user, Void.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
