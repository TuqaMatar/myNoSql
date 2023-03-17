package com.example.myNoSql;

import com.example.myNoSql.model.Node;
import com.example.myNoSql.model.Schema;
import com.fasterxml.jackson.databind.JsonNode;

public class ClusterDatabaseOperations implements DatabaseOperations {
    private Node assignedNode;

    public ClusterDatabaseOperations(Node assignedNode) {
        this.assignedNode = assignedNode;
    }

    @Override
    public void createDatabase(String databaseId, Schema schema) {
        // Implementation for creating a new database in the cluster
    }

    @Override
    public void deleteDatabase(String databaseId) {
        // Implementation for deleting a database from the cluster
    }

    @Override
    public void createDocument(String databaseId, String documentId, JsonNode data) {

    }

    @Override
    public void deleteDocument(String databaseId, String documentId) {

    }

    @Override
    public JsonNode readJsonProperty(String databaseId, String documentId, String propertyPath) {
        return null;
    }

    @Override
    public void writeJsonProperty(String databaseId, String documentId, String propertyPath, JsonNode value) {

    }

}
