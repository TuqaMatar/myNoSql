package com.example.myNoSql;

import com.example.myNoSql.model.Schema;
import com.fasterxml.jackson.databind.JsonNode;

public interface DatabaseOperations {
    // Create a new database with the given ID and schema
    void createDatabase(String databaseId, Schema schema);

    // Delete an existing database by its ID
    void deleteDatabase(String databaseId);

    // Create a new document in the specified database
    void createDocument(String databaseId, String documentId, JsonNode data);

    // Delete an existing document from the specified database
    void deleteDocument(String databaseId, String documentId);

    // Read a JSON property from a document in the specified database
    JsonNode readJsonProperty(String databaseId, String documentId, String propertyPath);

    // Write a JSON property to a document in the specified database
    void writeJsonProperty(String databaseId, String documentId, String propertyPath, JsonNode value);
}
