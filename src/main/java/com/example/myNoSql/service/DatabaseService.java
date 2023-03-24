package com.example.myNoSql.service;

import com.example.myNoSql.model.Database;
import com.example.myNoSql.model.Document;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class DatabaseService {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);
    private List<Database> databases;
    private ObjectMapper objectMapper;
    private String dataDirectory;


    public DatabaseService() {
        this.databases = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        this.dataDirectory = "./data";
        //loadDatabasesFromDisk();
    }

    @Async
    public CompletableFuture<Void> createDatabase(String dbName, JsonNode schema) {
        logger.info("Processing createDatabase request in thread: {}", Thread.currentThread().getName());
        Database newDatabase = new Database(dbName, schema);
        databases.add(newDatabase);
        //saveDatabaseToDisk(newDatabase);

        return CompletableFuture.completedFuture(null);
    }


    private void loadDatabasesFromDisk() {
        File dataDir = new File(dataDirectory);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        File[] databaseFiles = dataDir.listFiles();
        if (databaseFiles != null) {
            for (File dbFile : databaseFiles) {
                try {
                    Database database = objectMapper.readValue(new FileInputStream(dbFile), Database.class);
                    databases.add(database);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveDatabaseToDisk(Database database) {
        File dbFile = new File(dataDirectory, database.getName() + ".json");
        try {
            objectMapper.writeValue(new FileOutputStream(dbFile), database);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Async
    public CompletableFuture<Void> addDocumentToDatabase(String databaseName, Document document) {
        logger.info("Processing addDocumentToDatabase request in thread: {}", Thread.currentThread().getName());

        Database db = findDatabaseByName(databaseName);
        if (db == null) {
            throw new RuntimeException("Database not found");
        }
        //validate against schema
        JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

        try {
            JsonSchema jsonSchema = factory.getJsonSchema(db.getSchema());
            ProcessingReport report = jsonSchema.validate(document.getData());
            if (report.isSuccess()) {
                db.addDocument(document);
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
    public CompletableFuture<Void> deleteDocumentFromDatabase(String databaseName, Document document) {
        logger.info("Processing deleteDocumentFromDatabase request in thread: {}", Thread.currentThread().getName());

        Database db = findDatabaseByName(databaseName);
        if (db == null) {
            throw new RuntimeException("Database not found");
        }
        db.deleteDocument(document);

        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> updateDocumentFromDatabase(String databaseName, Integer documentId, Document document) {
        logger.info("Processing updateDocumentFromDatabase request in thread: {}", Thread.currentThread().getName());
        Database db = findDatabaseByName(databaseName);
        if (db == null) {
            throw new RuntimeException("Database not found");
        }
        db.updateDocument(documentId, document);
        return CompletableFuture.completedFuture(null);
    }

    public Database findDatabaseByName(String databaseName) {
        for (Database db : databases) {
            if (db.getName().equals(databaseName)) {
                return db;
            }
        }
        return null;
    }

    public CompletableFuture<List<Database>> getDatabases() {
        return CompletableFuture.completedFuture(databases);
    }


}