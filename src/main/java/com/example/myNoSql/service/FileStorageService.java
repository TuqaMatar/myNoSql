package com.example.myNoSql.service;

import com.example.myNoSql.model.Database;
import com.example.myNoSql.model.Document;
import com.example.myNoSql.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class FileStorageService {
    private final String basePath = "app/data/";
    private final ObjectMapper objectMapper;

    public FileStorageService() {
        objectMapper = new ObjectMapper();
    }

    public void saveDatabaseToFile(Database database) {
        String dbName = database.getName();
        Path dbPath = Paths.get(basePath, dbName);

        // Create the directory if it does not exist
        if (!Files.exists(dbPath)) {
            try {
                Files.createDirectories(dbPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Save the schema to a file
        File schemaFile = new File(dbPath.toString(), "schema.json");
        try {
            objectMapper.writeValue(schemaFile, database.getSchema());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDocumentToFile(String databaseName, Document document) {
        Path dbPath = Paths.get(basePath, databaseName);

        // Save the document to a file
        File documentFile = new File(dbPath.toString(), document.getId() + ".json");
        try {
            objectMapper.writeValue(documentFile, document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Database> loadDatabasesFromFileSystem() {
        List<Database> databases = new ArrayList<>();
        File basePathDir = new File(basePath);

        if (basePathDir.exists() && basePathDir.isDirectory()) {
            File[] dbDirs = basePathDir.listFiles(File::isDirectory);

            if (dbDirs != null) {
                for (File dbDir : dbDirs) {
                    File schemaFile = new File(dbDir, "schema.json");
                    if (schemaFile.exists()) {
                        try {
                            JsonNode schema = objectMapper.readTree(schemaFile);
                            Database database = new Database(dbDir.getName(), schema);
                            databases.add(database);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return databases;
    }

    public List<Document> loadDocumentsFromDirectory(String databaseName) {
        Path dbPath = Paths.get(basePath, databaseName);
        List<Document> documents = new ArrayList<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dbPath, "*.json")) {
            for (Path path : directoryStream) {
                if (!path.getFileName().toString().equals("schema.json")) {
                    Document document = objectMapper.readValue(path.toFile(), Document.class);
                    documents.add(document);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return documents;
    }

    public void deleteDocumentFromFile(String databaseName, Integer documentId) {
        Path dbPath = Paths.get(basePath, databaseName);
        File documentFile = new File(dbPath.toString(), documentId + ".json");

        if (documentFile.exists()) {
            documentFile.delete();
        }
    }

    public void saveUserToFile(User user) {
        String usersDir = "users";
        Path usersPath = Paths.get(basePath, usersDir);

        // Create the directory if it does not exist
        if (!Files.exists(usersPath)) {
            try {
                Files.createDirectories(usersPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Save the user to a file
        File userFile = new File(usersPath.toString(), user.getId() + ".json");
        try {
            objectMapper.writeValue(userFile, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<User> loadUsersFromFile() {
        String usersDir = "users";
        Path usersPath = Paths.get(basePath, usersDir);
        List<User> users = new ArrayList<>();

        if (Files.exists(usersPath)) {
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(usersPath, "*.json")) {
                for (Path userFile : directoryStream) {
                    User user = objectMapper.readValue(userFile.toFile(), User.class);
                    users.add(user);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return users;
    }


    public void saveIndexToFile(InvertedIndexService invertedIndexService){
        String indexesDir = "indexes";
        Path indexesPath = Paths.get(basePath, indexesDir);

        // Create the directory if it does not exist
        if (!Files.exists(indexesPath)) {
            try {
                Files.createDirectories(indexesPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Save the index to a file
        File indexFile = new File(indexesPath.toString(), "inverted_index.json");
        try {
            objectMapper.writeValue(indexFile, invertedIndexService.getIndex());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Set<Integer>> loadIndexFromFile() {
        String indexesDir = "indexes";
        Path indexesPath = Paths.get(basePath, indexesDir);
        Map<String, Set<Integer>> index = new HashMap<>();

        File indexFile = new File(indexesPath.toString(), "inverted_index.json");

        if (indexFile.exists()) {
            try {
                index = objectMapper.readValue(indexFile, new TypeReference<Map<String, Set<Integer>>>() {});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return index;
    }



}