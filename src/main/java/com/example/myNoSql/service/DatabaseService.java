package com.example.myNoSql.service;

import com.example.myNoSql.Constants;
import com.example.myNoSql.model.Database;
import com.example.myNoSql.model.Document;
import com.example.myNoSql.model.Node;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class DatabaseService {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    InvertedIndexService invertedIndexService;

    private List<Database> databases;

    public DatabaseService() {
        this.databases = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        databases = fileStorageService.loadDatabasesFromFileSystem();
        for (Database database : databases) {
            List<Document> documents = fileStorageService.loadDocumentsFromDirectory(database.getName());
            System.out.println(documents);
            for (Document document : documents) {
                database.getDocumentMap().put(document.getId(), document);
            }
        }

    }

    @Async
    public CompletableFuture<Void> createDatabase(String dbName, JsonNode schema) {
        logger.info("Processing createDatabase request in thread: {}", Thread.currentThread().getName());
        Database newDatabase = new Database(dbName, schema);
        if (!databases.contains(newDatabase)) {
            databases.add(newDatabase);
        }
        fileStorageService.saveDatabaseToFile(newDatabase);
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture<List<Document>> getDocumentsFromDatabase(String databaseName) {
        Database db = findDatabaseByName(databaseName);
        if (db == null) {
            throw new RuntimeException("Database not found");
        }

        return CompletableFuture.completedFuture(db.getDocuments());
    }

    public Document getDocumentFromDatabase(String databaseName, Integer documentId) {

        Database db = findDatabaseByName(databaseName);
        if (db == null) {
            throw new RuntimeException("Database not found");
        }
        return db.getDocumentMap().get(documentId);
    }

    public CompletableFuture<List<Database>> getDatabases() {
        return CompletableFuture.completedFuture(databases);
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

        Database db = findDatabaseByName(databaseName);
        if (db == null) {
            throw new RuntimeException("Database not found");
        }

        db.deleteDocument(documentId);
        fileStorageService.deleteDocumentFromFile(databaseName, documentId);

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

    public boolean deleteDatabase(String databaseName) {
        // Check if the database exists
        Database database = findDatabaseByName(databaseName);
        if (database == null) {
            return false;
        }

        // Delete all the documents associated with the database
        for (Document document : database.getDocumentMap().values()) {
            deleteDocumentFromDatabase(databaseName, document.getId());
        }

        // Remove the database from the databases map
        databases.remove(database);

        // Delete the database directory and its contents (documents, schema, etc.)
        try {
            Files.walk(Paths.get("app/data", databaseName))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

}