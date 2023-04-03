package com.example.myNoSql.service;

import com.example.myNoSql.CreateDatabaseRequest;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class BroadcastingService {

    @Value("${CONTAINER_NAME:unknown}")
    private String containerName;

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

    public void broadcastDeleteDocumentToAllContainers(String databaseName, Integer documentId) {
        Map<String, Integer> serviceNameToPort = new HashMap<>();
        serviceNameToPort.put("node-1", 8080);
        serviceNameToPort.put("node-2", 8080);
        ExecutorService executor = Executors.newFixedThreadPool(serviceNameToPort.size());
        for (Map.Entry<String, Integer> entry : serviceNameToPort.entrySet()) {
            executor.submit(() -> {
                try {
                    RestTemplate restTemplate = new RestTemplate();
                    String url = "http://" + entry.getKey() + ":" + entry.getValue() + "/api/db/" + databaseName + "/deleteDocument/"+documentId+"?broadcast=false";
                    System.out.println(url);
                    restTemplate.postForObject(url, null ,Void.class);
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
            if(entry.getKey().equals(containerName))
                continue;
            executor.submit(() -> {
                try {
                    RestTemplate restTemplate = new RestTemplate();
                    String url = "http://" + entry.getKey() + ":" + entry.getValue()+ "/api/db/" + databaseName + "/updateDocumentFromRedirect/" + documentId + "?broadcast=false&redirect=false";
                    System.out.println("broadcasting to : " + url);
                    restTemplate.postForEntity(url, jsonContent, Void.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void broadcastDeleteDatabaseToAllContainers(String databaseName) {
        Map<String, Integer> serviceNameToPort = new HashMap<>();
        serviceNameToPort.put("node-1", 8080);
        serviceNameToPort.put("node-2", 8080);
        ExecutorService executor = Executors.newFixedThreadPool(serviceNameToPort.size());
        for (Map.Entry<String, Integer> entry : serviceNameToPort.entrySet()) {
            executor.submit(() -> {
                try {
                    RestTemplate restTemplate = new RestTemplate();
                    String url = "http://" + entry.getKey() + ":" + entry.getValue() + "/api/db/deleteDatabase/"+databaseName+"?broadcast=false";
                    System.out.println(url);
                    restTemplate.delete(url, null ,Void.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        executor.shutdown();
    }
}