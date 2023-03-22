package com.example.myNoSql.controller;

import com.example.myNoSql.CreateDatabaseRequest;
import com.example.myNoSql.CreateDocumentRequest;
import com.example.myNoSql.DocumentStore;
import com.example.myNoSql.model.Database;
import com.example.myNoSql.model.Document;
import com.example.myNoSql.service.DatabaseService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
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

    // CRUD OPERATIONS ON DOCUMENTS

    @PostMapping("/{databaseName}/createDocument")
    public void createDocument(@PathVariable("databaseName") String databaseName, @RequestBody JsonNode jsonContent) {
        Document document = new Document(jsonContent);
        databaseService.addDocumentToDatabase(databaseName, document);
    }


    @PostMapping("/{databaseName}/deleteDocument")
    public void deleteDocument(@PathVariable("databaseName") String databaseName, @RequestBody JsonNode jsonContent) {
        Document document = new Document(jsonContent);
        databaseService.deleteDocumentFromDatabase(databaseName, document);
    }

    @PostMapping("/{databaseName}/updateDocument/{documentId}")
    public void updateDocument(@PathVariable("databaseName") String databaseName, @PathVariable("documentId") Integer documentId,
                               @RequestBody JsonNode jsonContent) {
        Document newDocument = new Document(jsonContent);

        databaseService.updateDocumentFromDatabase(databaseName, documentId , newDocument);
    }


}
