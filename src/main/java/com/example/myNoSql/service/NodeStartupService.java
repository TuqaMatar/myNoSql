package com.example.myNoSql.service;

import com.example.myNoSql.NodeRegistrationRequest;
import com.example.myNoSql.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service
public class NodeStartupService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${BOOTSTRAPPING_NODE_URL:http://localhost:8080}")
    private String bootstrappingNodeUrl;

    @Value("${NODE_ID:}")
    private String nodeId;

    @Value("${NODE_IP_ADDRESS:}")
    private String nodeIpAddress;

    @Value("${NODE_ROLE:}")
    private String nodeRole;

    @PostConstruct
    public void registerWithBootstrappingNode() {
        if (!"NODE".equals(nodeRole)) {
            return;
        }

        String url = bootstrappingNodeUrl + "/nodes/register";
        NodeRegistrationRequest request = new NodeRegistrationRequest();
        request.setId(nodeId);
        request.setIpAddress(nodeIpAddress);
        restTemplate.postForObject(url, request, Void.class);
    }
}
