package com.example.myNoSql.service;

import com.example.myNoSql.model.Database;
import com.example.myNoSql.model.Document;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseService {
    private List<Database> databases;
    private ObjectMapper objectMapper;
    private String dataDirectory;

    public DatabaseService() {
        this.databases = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        this.dataDirectory = "./data";
        //loadDatabasesFromDisk();
    }

    public void createDatabase(String dbName , JsonNode schema) {
        Database newDatabase = new Database(dbName , schema);
        databases.add(newDatabase);
        saveDatabaseToDisk(newDatabase);
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

    public void addDocumentToDatabase(String databaseName, Document document) {
        Database db = findDatabaseByName(databaseName);
        if (db == null) {
            throw new RuntimeException("Database not found");
        }
        db.addDocument(document);
    }

    public Database findDatabaseByName(String databaseName) {
        for (Database db : databases) {
            if (db.getName().equals(databaseName)) {
                return db;
            }
        }
        return null;
    }

    public List<Database> getDatabases() {
        return databases;
    }
}