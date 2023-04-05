package com.example.myNoSql.controller;

import com.example.myNoSql.BootStrapProperties;
import com.example.myNoSql.CreateDatabaseRequest;
import com.example.myNoSql.service.InvertedIndexService;
import com.example.myNoSql.model.Database;
import com.example.myNoSql.model.Document;
import com.example.myNoSql.model.Node;
import com.example.myNoSql.service.BroadcastingService;
import com.example.myNoSql.service.DatabaseService;
import com.example.myNoSql.service.FileStorageService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/database")
public class DatabaseController {

    @Autowired
    private InvertedIndexService invertedIndexService;
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private BootStrapProperties bootstrapProperties;

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private BroadcastingService broadcastingService;

    @Value("${CONTAINER_NAME:unknown}")
    private String containerName;

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

    @GetMapping("/{databaseName}/getDocuments")
    public CompletableFuture<List<Document>> getDocuments(@PathVariable("databaseName") String databaseName) {
        return databaseService.getDocumentsFromDatabase(databaseName);
    }


    @GetMapping("/{databaseName}/{documentId}")
    public Document getDocumentFromId(@PathVariable("databaseName") String databaseName, @PathVariable("documentId") Integer documentId) {
        return databaseService.getDocumentFromDatabase(databaseName, documentId);
    }


    @DeleteMapping("/deleteDatabase/{databaseName}")
    public ResponseEntity<Void> deleteDatabase(@PathVariable("databaseName") String databaseName , @RequestParam(value = "broadcast", defaultValue = "true") boolean broadcast) {

        if (broadcast) {
            broadcastingService.broadcastDeleteDatabaseToAllContainers(databaseName);
        }
        if (databaseService.deleteDatabase(databaseName)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

