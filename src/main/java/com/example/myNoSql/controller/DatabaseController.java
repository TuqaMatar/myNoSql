package com.example.myNoSql.controller;

import com.example.myNoSql.BootStrapProperties;
import com.example.myNoSql.CreateDatabaseRequest;
import com.example.myNoSql.CreateDocumentRequest;
import com.example.myNoSql.DocumentStore;
import com.example.myNoSql.model.Database;
import com.example.myNoSql.model.Document;
import com.example.myNoSql.service.BroadcastingService;
import com.example.myNoSql.service.DatabaseService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/db")
public class DatabaseController {

    @Autowired
    private BootStrapProperties bootstrapProperties;

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private BroadcastingService broadcastingService;

    private DocumentStore documentStore;

    public DatabaseController() {
    }

    @PostMapping("/createDatabase")
    public CompletableFuture<Void> createDatabase(@RequestBody CreateDatabaseRequest createDatabaseRequest, @RequestParam(value = "broadcast", defaultValue = "true") boolean broadcast) {
        String dbName = createDatabaseRequest.getName();
        JsonNode schema = createDatabaseRequest.getSchema();
        if (broadcast) {
            broadcastingService.broadcastCreateDataBaseToAllContainers(createDatabaseRequest);
        }
        return databaseService.createDatabase(dbName, schema);
    }

    @GetMapping("/getDatabases")
    public CompletableFuture<List<Database>> getDatabases() {
        return databaseService.getDatabases();
    }

    // CRUD OPERATIONS ON DOCUMENTS

    @PostMapping("/{databaseName}/createDocument")
    public CompletableFuture<Void> createDocument(@PathVariable("databaseName") String databaseName, @RequestBody JsonNode jsonContent, @RequestParam(value = "broadcast", defaultValue = "true") boolean broadcast) {
        Document document = new Document(jsonContent);
        if (broadcast) {
           broadcastingService.broadcastCreateDocumentToAllContainers(databaseName, jsonContent);
        }

        return databaseService.addDocumentToDatabase(databaseName, document);
    }



    @PostMapping("/{databaseName}/updateDocument/{documentId}")
    public CompletableFuture<Void> updateDocument(@PathVariable("databaseName") String databaseName, @PathVariable("documentId") Integer documentId,
                                                  @RequestBody JsonNode jsonContent, @RequestParam(value = "broadcast", defaultValue = "true") boolean broadcast) {
        Document newDocument = new Document(jsonContent);

        if (broadcast) {
            broadcastingService.broadcastUpdateDocumentToAllContainers(databaseName, documentId, jsonContent);
        }

        return databaseService.updateDocumentFromDatabase(databaseName, documentId, newDocument);
    }

    @PostMapping("/{databaseName}/deleteDocument")
    public CompletableFuture<Void> deleteDocument(@PathVariable("databaseName") String databaseName, @RequestBody JsonNode jsonContent, @RequestParam(value = "broadcast", defaultValue = "true") boolean broadcast) {
        Document document = new Document(jsonContent);

        if (broadcast) {
            broadcastingService.broadcastDeleteDocumentToAllContainers(databaseName, jsonContent);
        }

        return databaseService.deleteDocumentFromDatabase(databaseName, document);
    }


}
