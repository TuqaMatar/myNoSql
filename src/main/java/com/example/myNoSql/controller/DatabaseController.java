package com.example.myNoSql.controller;

import com.example.myNoSql.BootStrapProperties;
import com.example.myNoSql.CreateDatabaseRequest;
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
@RequestMapping("/api/db")
public class DatabaseController {

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
    // CRUD OPERATIONS ON DOCUMENTS

    @PostMapping("/{databaseName}/createDocument")
    public CompletableFuture<Void> createDocument(@PathVariable("databaseName") String databaseName, @RequestBody JsonNode jsonContent, @RequestParam(value = "broadcast", defaultValue = "true") boolean broadcast) {
        Document document = new Document(jsonContent);
        // Assign the affinity node
        Node affinityNode = getAffinityNode(document.getId());
        document.setAffinityNode(affinityNode);

        if (broadcast) {
            broadcastingService.broadcastCreateDocumentToAllContainers(databaseName, jsonContent);
        }

        return databaseService.addDocumentToDatabase(databaseName, document);
    }

    @PostMapping("/{databaseName}/updateDocument/{documentId}")
    public CompletableFuture<Void> updateDocument(@PathVariable("databaseName") String databaseName, @PathVariable("documentId") Integer documentId,
                                                  @RequestBody JsonNode jsonContent, @RequestParam(value = "broadcast", defaultValue = "true") boolean broadcast,
                                                  @RequestHeader(value = "redirect", defaultValue = "true") boolean redirected) {
        Document newDocument = new Document(jsonContent);

        // Check if the current node has affinity with the document
        Document oldDocument = databaseService.getDocumentFromDatabase(databaseName, documentId);
        newDocument.setAffinityNode(oldDocument.getAffinityNode());
        System.out.println("redirected value : " + redirected);

        if (!isCurrentNodePort(oldDocument.getAffinityNode())) {
            // Redirect the request to the affinity node
            System.out.println("IS NOT AFFINITY");
            databaseService.redirectToAffinityNode(databaseName, documentId, jsonContent, oldDocument.getAffinityNode());
            return CompletableFuture.completedFuture(null); // Return immediately after redirecting
        }

        System.out.println("IS  AFFINITY");

        if (broadcast) {
            broadcastingService.broadcastUpdateDocumentToAllContainers(databaseName, documentId, jsonContent);
        }

        return databaseService.updateDocumentFromDatabase(databaseName, documentId, newDocument);
    }

    @PostMapping("/{databaseName}/updateDocumentFromRedirect/{documentId}")
    public CompletableFuture<Void> updateDocumentFromRedirect(@PathVariable("databaseName") String databaseName, @PathVariable("documentId") Integer documentId,
                                                              @RequestBody JsonNode jsonContent, @RequestParam(value = "broadcast", defaultValue = "true") boolean broadcast, @RequestHeader(value = "redirect", defaultValue = "true") boolean redirected) {
        Document newDocument = new Document(jsonContent);
        // Check if the current node has affinity with the document
        Document oldDocument = databaseService.getDocumentFromDatabase(databaseName, documentId);
        newDocument.setAffinityNode(oldDocument.getAffinityNode());

        System.out.println("IN  AFFINITY");

        if (broadcast) {
            broadcastingService.broadcastUpdateDocumentToAllContainers(databaseName, documentId, jsonContent);
        }
        return databaseService.updateDocumentFromDatabase(databaseName, documentId, newDocument);
    }

    private boolean isCurrentNodePort(Node affinityNode) {
        System.out.println("affinity node : " + affinityNode.getName() + " container name: " + containerName);
        return affinityNode.getName().equals(containerName);
    }

    @PostMapping("/{databaseName}/deleteDocument/{documentId}")
    public CompletableFuture<Void> deleteDocument(@PathVariable("databaseName") String databaseName, @PathVariable("documentId") Integer documentId, @RequestParam(value = "broadcast", defaultValue = "true") boolean broadcast) {

        if (broadcast) {
            broadcastingService.broadcastDeleteDocumentToAllContainers(databaseName, documentId);
        }
        return databaseService.deleteDocumentFromDatabase(databaseName, documentId);
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


    private Node getAffinityNode(Integer documentId) {
        List<Node> nodes = bootstrapProperties.getNodes();
        int numberOfNodes = nodes.size();

        // Determine the affinity node based on the document ID
        int affinityIndex = Math.abs(documentId % numberOfNodes);

        // Return the affinity node's address
        return nodes.get(affinityIndex);
    }
}

