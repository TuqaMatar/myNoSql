package com.example.myNoSql.service;

import com.example.myNoSql.BootStrapProperties;
import com.example.myNoSql.Constants;
import com.example.myNoSql.CreateDatabaseRequest;
import com.example.myNoSql.model.Node;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class BroadcastingService {
    List<Node> nodes;
    @Value("${CONTAINER_NAME:unknown}")
    private String containerName;
    @Autowired
    private BootStrapProperties bootstrapProperties;

    public BroadcastingService() {

    }

    public void broadcastCreateDataBaseToAllContainers(CreateDatabaseRequest createDatabaseRequest) {
        nodes = bootstrapProperties.getNodes();
        ExecutorService executor = Executors.newFixedThreadPool(nodes.size());
        for (Node node : nodes) {
            executor.submit(() -> {
                try {
                    RestTemplate restTemplate = new RestTemplate();
                    String url = "http://" + node.getName() + ":" + Constants.CONTAINER_PORT + "/api/db/createDatabase?broadcast=false";
                    System.out.println(url);
                    restTemplate.postForEntity(url, createDatabaseRequest, Void.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
    }

    public void broadcastCreateDocumentToAllContainers(String databaseName, JsonNode jsonContent) {
        nodes = bootstrapProperties.getNodes();
        ExecutorService executor = Executors.newFixedThreadPool(nodes.size());
        for (Node node : nodes) {
            executor.submit(() -> {
                try {
                    RestTemplate restTemplate = new RestTemplate();
                    String url = "http://" + node.getName() + ":" + Constants.CONTAINER_PORT + "/api/db/" + databaseName + "/createDocument?broadcast=false";
                    HttpEntity<JsonNode> entity = new HttpEntity<>(jsonContent);
                    restTemplate.postForEntity(url, entity, Void.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        // Shutdown the executor after all tasks are done
        executor.shutdown();
    }

    public void broadcastDeleteDocumentToAllContainers(String databaseName, Integer documentId) {
        nodes = bootstrapProperties.getNodes();
        ExecutorService executor = Executors.newFixedThreadPool(nodes.size());
        for (Node node : nodes) {
            executor.submit(() -> {
                try {
                    RestTemplate restTemplate = new RestTemplate();
                    String url = "http://" + node.getName() + ":" + Constants.CONTAINER_PORT + "/api/db/" + databaseName + "/deleteDocument/" + documentId + "?broadcast=false";
                    System.out.println(url);
                    restTemplate.postForObject(url, null, Void.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
    }

    public void broadcastUpdateDocumentToAllContainers(String databaseName, Integer documentId, JsonNode jsonContent) {

        nodes = bootstrapProperties.getNodes();
        ExecutorService executor = Executors.newFixedThreadPool(nodes.size());
        for (Node node :nodes) {
            if (node.getName().equals(containerName))
                continue;
            executor.submit(() -> {
                try {
                    RestTemplate restTemplate = new RestTemplate();
                    String url = "http://" + node.getName() + ":" + Constants.CONTAINER_PORT + "/api/db/" + databaseName + "/updateDocumentFromRedirect/" + documentId + "?broadcast=false&redirect=false";
                    System.out.println("broadcasting to : " + url);
                    restTemplate.postForEntity(url, jsonContent, Void.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void broadcastDeleteDatabaseToAllContainers(String databaseName) {

        ExecutorService executor = Executors.newFixedThreadPool(nodes.size());
        for (Node node : nodes) {
            executor.submit(() -> {
                try {
                    RestTemplate restTemplate = new RestTemplate();
                    String url = "http://" + node.getName() + ":" + Constants.CONTAINER_PORT + "/api/db/deleteDatabase/" + databaseName + "?broadcast=false";
                    System.out.println(url);
                    restTemplate.delete(url, null, Void.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
    }
}