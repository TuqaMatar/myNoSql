package com.example.myNoSql.model;

import com.fasterxml.jackson.databind.JsonNode;

public class Schema {
    private String id;
    private JsonNode schemaDefinition;

    public Schema(String id, JsonNode schemaDefinition) {
        this.id = id;
        this.schemaDefinition = schemaDefinition;
    }

    // Getters, setters, and other methods
}
