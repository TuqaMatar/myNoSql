package com.example.myNoSql.model;

import com.fasterxml.jackson.databind.JsonNode;

public class Document {
    private String id;
    private JsonNode data;

    public Document(String id, JsonNode data) {
        this.id = id;
        this.data = data;
    }

    // Getters, setters, and other methods
}
