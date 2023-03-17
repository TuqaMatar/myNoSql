package com.example.myNoSql.model;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private String id;
    private Schema schema;
    private Map<String, Document> documents;

    public Database(String id, Schema schema) {
        this.id = id;
        this.schema = schema;
        this.documents = new HashMap<>();
    }

    // Getters, setters, and other methods
}
