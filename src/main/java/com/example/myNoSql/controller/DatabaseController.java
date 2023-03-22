package com.example.myNoSql.controller;

import com.example.myNoSql.CreateDatabaseRequest;
import com.example.myNoSql.CreateDocumentRequest;
import com.example.myNoSql.DocumentStore;
import com.example.myNoSql.model.Database;
import com.example.myNoSql.model.Document;
import com.example.myNoSql.service.DatabaseService;
import com.fasterxml.jackson.databind.JsonNode;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/db")
public class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    private DocumentStore documentStore;

    public DatabaseController() {
    }

    @PostMapping("/addDocument")
    public void addDocument(@RequestBody CreateDocumentRequest createDocumentRequest) {
        String databaseName = createDocumentRequest.getDatabaseName();
        Document document = createDocumentRequest.getDocument();
        databaseService.addDocumentToDatabase(databaseName, document);
    }

    @PostMapping("/createDatabase")
    public void createDatabase(@RequestBody CreateDatabaseRequest createDatabaseRequest) {
        String dbName = createDatabaseRequest.getName();
        JsonNode schema = createDatabaseRequest.getSchema();
        databaseService.createDatabase(dbName, schema);
    }

    @GetMapping("/getDatabases")
    public List<Database> getDatabases() {
        return databaseService.getDatabases();
    }

    @PostMapping("/{databaseName}/addDocument")
    public void addDocument(@PathVariable("databaseName") String databaseName, @RequestBody JsonNode jsonContent) {
        Document document = new Document(jsonContent);
        databaseService.addDocumentToDatabase(databaseName, document);
    }
    // Add more methods for other CRUD operations on databases and documents
}
