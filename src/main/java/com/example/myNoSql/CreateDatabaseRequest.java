package com.example.myNoSql;

import com.fasterxml.jackson.databind.JsonNode;

public class CreateDatabaseRequest {
    private String name;
    private JsonNode schema;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonNode getSchema() {
        return schema;
    }

    public void setSchema(JsonNode schema) {
        this.schema = schema;
    }
}