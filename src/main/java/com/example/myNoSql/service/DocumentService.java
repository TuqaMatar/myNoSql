package com.example.myNoSql.service;

import com.example.myNoSql.Constants;
import com.example.myNoSql.model.Database;
import com.example.myNoSql.model.Document;
import com.example.myNoSql.model.Node;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    @Autowired
    FileStorageService fileStorageService;
    @Autowired
    InvertedIndexService invertedIndexService;
    @Autowired
    DatabaseService databaseService ;

    @PostConstruct
    public void initIndex() {
        Map<String, Set<Integer>> loadedIndex = fileStorageService.loadIndexFromFile();

        // Add the loaded index to the new index
        for (Map.Entry<String, Set<Integer>> entry : loadedIndex.entrySet()) {
            for (Integer docId : entry.getValue()) {
                invertedIndexService.getIndex().computeIfAbsent(entry.getKey(), k -> new HashSet<>()).add(docId);
            }
        }
    }

    public List<Document> searchDocumentsByKeyword(String keyword , String databaseName) {

        Database db = databaseService.findDatabaseByName(databaseName);
        if (db == null) {
            throw new RuntimeException("Database not found");
        }

        List<Document> databaseDocuments = db.getDocuments();

        Set<Integer> documentIds = invertedIndexService.search(keyword);

        return databaseDocuments.stream()
                .filter(document -> documentIds.contains(document.getId()))
                .collect(Collectors.toList());
    }

    public Document getDocumentFromDatabase(String databaseName, Integer documentId) {

        Database db = databaseService.findDatabaseByName(databaseName);
        if (db == null) {
            throw new RuntimeException("Database not found");
        }
        return db.getDocumentMap().get(documentId);
    }



    @Async
    public CompletableFuture<Void> addDocumentToDatabase(String databaseName, Document document) {
        logger.info("Processing addDocumentToDatabase request in thread: {}", Thread.currentThread().getName());

        Database db = databaseService.findDatabaseByName(databaseName);
        if (db == null) {
            throw new RuntimeException("Database not found");
        }
        JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

        try {
            JsonSchema jsonSchema = factory.getJsonSchema(db.getSchema());
            ProcessingReport report = jsonSchema.validate(document.getData());
            if (report.isSuccess()) {
                invertedIndexService.addDocument(document);

                db.addDocument(document);
                fileStorageService.saveDocumentToFile(databaseName, document);
                fileStorageService.saveIndexToFile(invertedIndexService);
            } else {
                System.out.println("Document validation failed:");
                System.out.println(report);
            }
        } catch (ProcessingException e) {
            e.printStackTrace();
        }

        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> deleteDocumentFromDatabase(String databaseName, Integer documentId) {
        logger.info("Processing deleteDocumentFromDatabase request in thread: {}", Thread.currentThread().getName());

        Database db = databaseService.findDatabaseByName(databaseName);
        if (db == null) {
            throw new RuntimeException("Database not found");
        }

        db.deleteDocument(documentId);
        fileStorageService.deleteDocumentFromFile(databaseName, documentId);

        return CompletableFuture.completedFuture(null);
    }

    @Async
    public synchronized CompletableFuture<Void> updateDocumentFromDatabase(String databaseName, Integer documentId, Document document) {
        logger.info("Processing updateDocumentFromDatabase request in thread: {}", Thread.currentThread().getName());
        System.out.println("IN UPDATE");
        Database db = databaseService.findDatabaseByName(databaseName);
        if (db == null) {
            throw new RuntimeException("Database not found");
        }
        db.updateDocument(documentId, document);
        return CompletableFuture.completedFuture(null);
    }


    public void redirectToAffinityNode(String databaseName, Integer documentId, JsonNode jsonContent, Node affinityNode) {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(() -> {
            try {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.add("X-Redirected", "true"); // Add custom header
                HttpEntity<JsonNode> entity = new HttpEntity<>(jsonContent, headers); // Pass the custom header

                String url = "http://" + affinityNode.getName() + ":" + Constants.CONTAINER_PORT + "/api/document/" + databaseName + "/updateDocumentFromRedirect/" + documentId + "?broadcast=true&redirect=false";
                System.out.println("re-directing to : " + url);
                restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        executor.shutdown();

    }
}
