package com.example.myNoSql.service;

import com.example.myNoSql.CreateDatabaseRequest;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class BroadcastingService {


    public void broadcastCreateDataBaseToAllContainers(CreateDatabaseRequest createDatabaseRequest) {
        Map<String, Integer> serviceNameToPort = new HashMap<>();
        serviceNameToPort.put("node-1", 8080);
        serviceNameToPort.put("node-2", 8080);

        // Use an ExecutorService to send the requests asynchronously
        ExecutorService executor = Executors.newFixedThreadPool(serviceNameToPort.size());
        for (Map.Entry<String, Integer> entry : serviceNameToPort.entrySet()) {
            executor.submit(() -> {
                try {
                    RestTemplate restTemplate = new RestTemplate();
                    String url = "http://" + entry.getKey() + ":" + entry.getValue() + "/api/db/createDatabase?broadcast=false";
                    restTemplate.postForEntity(url, createDatabaseRequest, Void.class);
                } catch (Exception e) {
                    // Log the error
                    e.printStackTrace();
                }
            });
        }

        // Shutdown the executor after all tasks are done
        executor.shutdown();
    }

    public void broadcastCreateDocumentToAllContainers(String databaseName, JsonNode jsonContent) {
        Map<String, Integer> serviceNameToPort = new HashMap<>();
        serviceNameToPort.put("node-1", 8080);
        serviceNameToPort.put("node-2", 8080);

        // Use an ExecutorService to send the requests asynchronously
        ExecutorService executor = Executors.newFixedThreadPool(serviceNameToPort.size());
        for (Map.Entry<String, Integer> entry : serviceNameToPort.entrySet()) {
            executor.submit(() -> {
                try {
                    RestTemplate restTemplate = new RestTemplate();
                    String url = "http://" + entry.getKey() + ":" + entry.getValue() + "/api/db/" + databaseName + "/createDocument?broadcast=false";
                    HttpEntity<JsonNode> entity = new HttpEntity<>(jsonContent);
                    restTemplate.postForEntity(url, entity, Void.class);
                } catch (Exception e) {
                    // Log the error
                    e.printStackTrace();
                }
            });
        }

        // Shutdown the executor after all tasks are done
        executor.shutdown();
    }

    public void broadcastDeleteDocumentToAllContainers(String databaseName, JsonNode jsonContent) {
        Map<String, Integer> serviceNameToPort = new HashMap<>();
        serviceNameToPort.put("node-1", 8080);
        serviceNameToPort.put("node-2", 8080);
        ExecutorService executor = Executors.newFixedThreadPool(serviceNameToPort.size());
        for (Map.Entry<String, Integer> entry : serviceNameToPort.entrySet()) {
            executor.submit(() -> {
                try {
                    RestTemplate restTemplate = new RestTemplate();
                    String url = "http://" + entry.getKey() + ":" + entry.getValue() + "/api/db/" + databaseName + "/deleteDocument?broadcast=false";
                    restTemplate.postForEntity(url, jsonContent, Void.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
    }
    public void broadcastUpdateDocumentToAllContainers(String databaseName, Integer documentId, JsonNode jsonContent) {
        Map<String, Integer> serviceNameToPort = new HashMap<>();
        serviceNameToPort.put("node-1", 8080);
        serviceNameToPort.put("node-2", 8080);
        ExecutorService executor = Executors.newFixedThreadPool(serviceNameToPort.size());
        for (Map.Entry<String, Integer> entry : serviceNameToPort.entrySet()) {
            executor.submit(() -> {
                try {
                    RestTemplate restTemplate = new RestTemplate();
                    String url = "http://" + entry.getKey() + ":" + entry.getValue()+ "/api/db/" + databaseName + "/updateDocument/" + documentId + "?broadcast=false";
                    restTemplate.postForEntity(url, jsonContent, Void.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}